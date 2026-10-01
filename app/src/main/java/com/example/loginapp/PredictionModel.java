package com.example.loginapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PredictionModel {

    public static class PredictionResult {
        private final List<SavingsAnalytics.ChartDataPoint> chartPoints;
        private final String estimatedEta;
        private final String statusBadge;
        private final double predictedNextMonth;
        private final double projectedYearlySavings;

        public PredictionResult(List<SavingsAnalytics.ChartDataPoint> chartPoints, String estimatedEta, String statusBadge, double predictedNextMonth, double projectedYearlySavings) {
            this.chartPoints = chartPoints;
            this.estimatedEta = estimatedEta;
            this.statusBadge = statusBadge;
            this.predictedNextMonth = predictedNextMonth;
            this.projectedYearlySavings = projectedYearlySavings;
        }

        public List<SavingsAnalytics.ChartDataPoint> getChartPoints() {
            return chartPoints;
        }

        public String getEstimatedEta() {
            return estimatedEta;
        }

        public String getStatusBadge() {
            return statusBadge;
        }

        public double getPredictedNextMonth() {
            return predictedNextMonth;
        }

        public double getProjectedYearlySavings() {
            return projectedYearlySavings;
        }
    }

    public static PredictionResult calculatePrediction(List<SavingsAnalytics.ChartDataPoint> historicalPoints, double totalSaved, double totalTarget, List<SavingPlan> plans) {
        List<SavingsAnalytics.ChartDataPoint> combinedPoints = new ArrayList<>();

        // Add historical actual points
        double historicalSum = 0;
        int historicalCount = 0;
        if (historicalPoints != null) {
            for (SavingsAnalytics.ChartDataPoint dp : historicalPoints) {
                combinedPoints.add(new SavingsAnalytics.ChartDataPoint(dp.getLabel(), dp.getValue(), false));
                historicalSum += dp.getValue();
                historicalCount++;
            }
        }

        // Determine monthly savings velocity
        double monthlyVelocity = 0;
        if (historicalCount > 0 && historicalSum > 0) {
            monthlyVelocity = historicalSum / historicalCount;
        } else if (plans != null && !plans.isEmpty()) {
            // Plan-based velocity fallback
            for (SavingPlan plan : plans) {
                double planMonthlyRate = plan.getAllowanceAmount();
                if ("Daily".equalsIgnoreCase(plan.getFrequency())) {
                    planMonthlyRate *= 30;
                } else if ("Weekly".equalsIgnoreCase(plan.getFrequency())) {
                    planMonthlyRate *= 4.34;
                }
                monthlyVelocity += planMonthlyRate * 0.20; // 20% estimated saving rate
            }
        }

        if (monthlyVelocity <= 0) {
            monthlyVelocity = 500.0; // Reasonable default fallback velocity
        }

        // Project upcoming 3 months
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.US);

        double predictedNextMonth = monthlyVelocity;

        for (int i = 1; i <= 3; i++) {
            cal.add(Calendar.MONTH, 1);
            String monthLabel = monthFormat.format(cal.getTime());
            combinedPoints.add(new SavingsAnalytics.ChartDataPoint(monthLabel + "*", Math.round(monthlyVelocity), true));
        }

        // Calculate Target ETA
        double remainingTarget = Math.max(0, totalTarget - totalSaved);
        String estimatedEta;
        String statusBadge;

        if (totalTarget > 0 && remainingTarget <= 0) {
            estimatedEta = "Target Achieved";
            statusBadge = "Goal Achieved 🎉";
        } else {
            int monthsNeeded = (int) Math.ceil(remainingTarget / monthlyVelocity);
            if (monthsNeeded <= 0) monthsNeeded = 1;

            Calendar etaCal = Calendar.getInstance();
            etaCal.add(Calendar.MONTH, monthsNeeded);
            SimpleDateFormat etaFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
            estimatedEta = "Est. " + etaFormat.format(etaCal.getTime());

            statusBadge = "On Track 🚀";
        }

        double projectedYearlySavings = monthlyVelocity * 12;
        return new PredictionResult(combinedPoints, estimatedEta, statusBadge, predictedNextMonth, projectedYearlySavings);
    }
}
