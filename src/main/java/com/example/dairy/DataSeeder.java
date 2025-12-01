package com.example.dairy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CowRepository cowRepository;
    private final MilkProductionRepository productionRepository;

    public DataSeeder(CowRepository cowRepository, MilkProductionRepository productionRepository) {
        this.cowRepository = cowRepository;
        this.productionRepository = productionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (cowRepository.count() > 0) {
            System.out.println("Database already contains data. Skipping seed.");
            return;
        }

        System.out.println("🌱 Seeding sample data...");

        // Add Sample Cows
        cowRepository.save(new Cow("Bessie", "Holstein", LocalDate.of(2019, 5, 12)));
        cowRepository.save(new Cow("Daisy", "Jersey", LocalDate.of(2020, 8, 23)));
        cowRepository.save(new Cow("Molly", "Guernsey", LocalDate.of(2021, 2, 15)));
        cowRepository.save(new Cow("Bella", "Holstein", LocalDate.of(2018, 11, 30)));
        cowRepository.save(new Cow("Luna", "Ayrshire", LocalDate.of(2022, 1, 10)));

        // Fetch the cows we just added to get their IDs
        List<Cow> cows = cowRepository.findAll();

        // Add Production Records for the current month (up to today)
        LocalDate today = LocalDate.now();
        Random rand = new Random();

        // Generate data for the current month (1st to today)
        LocalDate startDate = today.withDayOfMonth(1);

        // If today is the 1st, generate for previous month too so we have something to
        // show if user looks back
        if (today.getDayOfMonth() == 1) {
            startDate = today.minusMonths(1).withDayOfMonth(1);
        }

        for (LocalDate date = startDate; !date.isAfter(today); date = date.plusDays(1)) {
            for (Cow cow : cows) {
                // Morning Shift
                // Randomize quantity based on cow (some are better producers)
                double baseQty = 10 + (cow.getCowId() % 3) * 2;
                double qtyM = baseQty + rand.nextDouble() * 4; // Variance
                double fatM = 3.5 + rand.nextDouble();

                productionRepository.save(new MilkProductionRecord(cow.getCowId(), date, "Morning",
                        Math.round(qtyM * 100.0) / 100.0,
                        Math.round(fatM * 100.0) / 100.0,
                        37.0, "Good"));

                // Evening Shift (usually slightly less)
                double qtyE = (baseQty * 0.8) + rand.nextDouble() * 3;
                double fatE = 3.8 + rand.nextDouble();

                productionRepository.save(new MilkProductionRecord(cow.getCowId(), date, "Evening",
                        Math.round(qtyE * 100.0) / 100.0,
                        Math.round(fatE * 100.0) / 100.0,
                        37.5, "Good"));
            }
        }

        System.out.println("✅ Sample data seeded successfully!");
    }
}
