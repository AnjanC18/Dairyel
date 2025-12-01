package com.example.dairy;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "milk_production")
public class MilkProductionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private int recordId;

    @Column(name = "cow_id")
    private int cowId;

    @Column(name = "production_date")
    private LocalDate productionDate;

    private String shift;
    private double quantity;

    @Column(name = "fat_content")
    private double fatContent;

    private double temperature;
    private String quality;

    public MilkProductionRecord() {
    }

    public MilkProductionRecord(int cowId, LocalDate productionDate, String shift,
            double quantity, double fatContent, double temperature, String quality) {
        this.cowId = cowId;
        this.productionDate = productionDate;
        this.shift = shift;
        this.quantity = quantity;
        this.fatContent = fatContent;
        this.temperature = temperature;
        this.quality = quality;
    }

    public MilkProductionRecord(int recordId, int cowId, LocalDate productionDate, String shift,
            double quantity, double fatContent, double temperature, String quality) {
        this.recordId = recordId;
        this.cowId = cowId;
        this.productionDate = productionDate;
        this.shift = shift;
        this.quantity = quantity;
        this.fatContent = fatContent;
        this.temperature = temperature;
        this.quality = quality;
    }

    // Getters and Setters
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getCowId() {
        return cowId;
    }

    public void setCowId(int cowId) {
        this.cowId = cowId;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getFatContent() {
        return fatContent;
    }

    public void setFatContent(double fatContent) {
        this.fatContent = fatContent;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    @Override
    public String toString() {
        return String.format("Date: %s | Shift: %s | Qty: %.2fL | Fat: %.1f%% | Quality: %s",
                productionDate, shift, quantity, fatContent, quality);
    }
}
