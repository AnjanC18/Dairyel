package com.example.dairy;

public class MonthlyStats {
    private int cowId;
    private String cowName;
    private int month;
    private int year;
    private Double totalMilk;
    private Double morningMilk;
    private Double eveningMilk;
    private Double averageFat;
    private long recordCount;

    public MonthlyStats(int cowId, String cowName, int month, int year, Double totalMilk,
            Double morningMilk, Double eveningMilk, Double averageFat, long recordCount) {
        this.cowId = cowId;
        this.cowName = cowName;
        this.month = month;
        this.year = year;
        this.totalMilk = totalMilk != null ? totalMilk : 0.0;
        this.morningMilk = morningMilk != null ? morningMilk : 0.0;
        this.eveningMilk = eveningMilk != null ? eveningMilk : 0.0;
        this.averageFat = averageFat != null ? averageFat : 0.0;
        this.recordCount = recordCount;
    }

    // Getters
    public int getCowId() {
        return cowId;
    }

    public String getCowName() {
        return cowName;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public double getTotalMilk() {
        return totalMilk;
    }

    public double getMorningMilk() {
        return morningMilk;
    }

    public double getEveningMilk() {
        return eveningMilk;
    }

    public double getAverageFat() {
        return averageFat;
    }

    public long getRecordCount() {
        return recordCount;
    }
}
