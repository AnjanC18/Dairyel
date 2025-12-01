package com.example.dairy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Controller
public class DairyController {

    private final CowRepository cowRepository;
    private final MilkProductionRepository productionRepository;

    public DairyController(CowRepository cowRepository, MilkProductionRepository productionRepository) {
        this.cowRepository = cowRepository;
        this.productionRepository = productionRepository;
    }

    @GetMapping("/")
    public String home(@RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Model model) {
        if (month == null)
            month = LocalDate.now().getMonthValue();
        if (year == null)
            year = LocalDate.now().getYear();

        List<MonthlyStats> stats = calculateMonthlyStats(month, year);
        model.addAttribute("stats", stats);

        // Calculate Dashboard Metrics
        double totalProduction = stats.stream().mapToDouble(MonthlyStats::getTotalMilk).sum();
        double overallAvgFat = stats.stream().mapToDouble(s -> s.getAverageFat() * s.getTotalMilk()).sum()
                / totalProduction;
        if (Double.isNaN(overallAvgFat))
            overallAvgFat = 0.0;

        MonthlyStats topCow = stats.stream()
                .max((s1, s2) -> Double.compare(s1.getTotalMilk(), s2.getTotalMilk()))
                .orElse(null);

        model.addAttribute("totalProduction", totalProduction);
        model.addAttribute("overallAvgFat", overallAvgFat);
        model.addAttribute("topCow", topCow);
        model.addAttribute("month", month);
        model.addAttribute("year", year);

        return "index";
    }

    @GetMapping("/cows")
    public String viewCows(Model model) {
        List<Cow> cows = cowRepository.findAll();
        model.addAttribute("cows", cows);
        model.addAttribute("newCow", new Cow("", "", LocalDate.now()));
        return "cows";
    }

    @PostMapping("/cows/add")
    public String addCow(@ModelAttribute Cow cow) {
        cowRepository.save(cow);
        return "redirect:/cows";
    }

    @PostMapping("/cows/delete/{id}")
    public String deleteCow(@PathVariable int id) {
        // Delete production records first (manual cascade if not handled by DB)
        productionRepository.deleteByCowId(id);
        cowRepository.deleteById(id);
        return "redirect:/cows";
    }

    @GetMapping("/production")
    public String viewProduction(@RequestParam(required = false) String date, Model model) {
        LocalDate viewDate = (date == null || date.isEmpty()) ? LocalDate.now() : LocalDate.parse(date);
        List<MilkProductionRecord> records = productionRepository.findByProductionDate(viewDate);
        model.addAttribute("records", records);
        model.addAttribute("viewDate", viewDate);
        model.addAttribute("cows", cowRepository.findAll()); // For the dropdown
        return "production";
    }

    @PostMapping("/production/add")
    public String recordProduction(
            @RequestParam int cowId,
            @RequestParam String date,
            @RequestParam String shift,
            @RequestParam double quantity,
            @RequestParam double fatContent,
            @RequestParam double temperature) {

        String quality = calculateQuality(fatContent, temperature);
        MilkProductionRecord record = new MilkProductionRecord(cowId, LocalDate.parse(date), shift, quantity,
                fatContent, temperature, quality);
        productionRepository.save(record);
        return "redirect:/production?date=" + date;
    }

    @GetMapping("/analysis")
    public String viewAnalysis(@RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Model model) {
        if (month == null)
            month = LocalDate.now().getMonthValue();
        if (year == null)
            year = LocalDate.now().getYear();

        List<MonthlyStats> stats = calculateMonthlyStats(month, year);

        model.addAttribute("stats", stats);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        return "analysis";
    }

    @PostMapping("/analysis/export")
    public String exportAnalysis(@RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            RedirectAttributes redirectAttributes) {
        if (month == null)
            month = LocalDate.now().getMonthValue();
        if (year == null)
            year = LocalDate.now().getYear();

        List<MonthlyStats> stats = calculateMonthlyStats(month, year);

        String folderPath = "D:\\Documents\\DairyReport";
        String fileName = "Monthly_Report_" + month + "_" + year + ".csv";
        Path path = Paths.get(folderPath);

        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(Paths.get(folderPath, fileName).toFile()))) {
                writer.println("Cow Name,Total Milk (L),Morning (L),Evening (L),Avg Fat %,Records");
                for (MonthlyStats stat : stats) {
                    writer.printf("%s,%.2f,%.2f,%.2f,%.2f,%d%n",
                            stat.getCowName(),
                            stat.getTotalMilk(),
                            stat.getMorningMilk(),
                            stat.getEveningMilk(),
                            stat.getAverageFat(),
                            stat.getRecordCount());
                }
            }

            redirectAttributes.addFlashAttribute("message",
                    "Report exported successfully to " + folderPath + "\\" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to export report: " + e.getMessage());
        }

        return "redirect:/analysis?month=" + month + "&year=" + year;
    }

    private List<MonthlyStats> calculateMonthlyStats(int month, int year) {
        System.out.println("Calculating stats for Month: " + month + ", Year: " + year);
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<MilkProductionRecord> records = productionRepository.findByProductionDateBetween(startDate, endDate);
        List<Cow> cows = cowRepository.findAll();

        return cows.stream().map(cow -> {
            List<MilkProductionRecord> cowRecords = records.stream()
                    .filter(r -> r.getCowId() == cow.getCowId())
                    .toList();

            double totalMilk = cowRecords.stream().mapToDouble(MilkProductionRecord::getQuantity).sum();
            double morningMilk = cowRecords.stream().filter(r -> "Morning".equals(r.getShift()))
                    .mapToDouble(MilkProductionRecord::getQuantity).sum();
            double eveningMilk = cowRecords.stream().filter(r -> "Evening".equals(r.getShift()))
                    .mapToDouble(MilkProductionRecord::getQuantity).sum();
            double avgFat = cowRecords.stream().mapToDouble(MilkProductionRecord::getFatContent).average().orElse(0.0);
            long count = cowRecords.size();

            return new MonthlyStats(cow.getCowId(), cow.getName(), month, year, totalMilk, morningMilk, eveningMilk,
                    avgFat, count);
        }).toList();
    }

    private String calculateQuality(double fat, double temp) {
        if (fat >= 4.0 && temp <= 7.0)
            return "Good";
        if (fat >= 3.5)
            return "Average";
        return "Poor";
    }
}
