package com.example.loginapp;

import java.util.List;

public class SavingsAnalytics {

    public static class ChartDataPoint {
        private final String label;
        private final double value;
        private final boolean isPredicted;

        public ChartDataPoint(String label, double value) {
            this(label, value, false);
        }

        public ChartDataPoint(String label, double value, boolean isPredicted) {
            this.label = label;
            this.value = value;
            this.isPredicted = isPredicted;
        }

        public String getLabel() {
            return label;
        }

        public double getValue() {
            return value;
        }

        public boolean isPredicted() {
            return isPredicted;
        }
    }

    private final List<ChartDataPoint> dataPoints;
    private final double averageSavings;
    private final double highestSavings;
    private final double completionRate;

    public SavingsAnalytics(List<ChartDataPoint> dataPoints, double averageSavings, double highestSavings, double completionRate) {
        this.dataPoints = dataPoints;
        this.averageSavings = averageSavings;
        this.highestSavings = highestSavings;
        this.completionRate = completionRate;
    }

    public List<ChartDataPoint> getDataPoints() {
        return dataPoints;
    }

    public double getAverageSavings() {
        return averageSavings;
    }

    public double getHighestSavings() {
        return highestSavings;
    }

    public double getCompletionRate() {
        return completionRate;
    }
}
