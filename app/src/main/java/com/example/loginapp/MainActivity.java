package com.example.loginapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.loginapp.databinding.ActivityMainBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatabaseHelper dbHelper;
    private SavingPlansAdapter adapter;
    private List<SavingPlan> planList;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        userId = getSharedPreferences("SavingslyPrefs", MODE_PRIVATE).getInt("userId", -1);

        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        binding.rvPlans.setLayoutManager(new LinearLayoutManager(this));
        planList = new ArrayList<>();
        adapter = new SavingPlansAdapter(planList, dbHelper, plan -> {
            Intent intent = new Intent(MainActivity.this, PlanDetailsActivity.class);
            intent.putExtra("planId", plan.getId());
            intent.putExtra("planName", plan.getName());
            intent.putExtra("planTarget", plan.getTargetAmount());
            intent.putExtra("planFrequency", plan.getFrequency());
            intent.putExtra("planEndDate", plan.getEndDate());
            intent.putExtra("planAllowance", plan.getAllowanceAmount());
            startActivity(intent);
        });
        binding.rvPlans.setAdapter(adapter);

        binding.btnDashboardAddSaving.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddPlanActivity.class)));

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_add) {
                startActivity(new Intent(MainActivity.this, AddPlanActivity.class));
                return true;
            }
            return true;
        });

        loadDashboard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboard();
    }

    private void loadDashboard() {
        String userName = dbHelper.getUserName(userId);
        if (userName == null || userName.isEmpty()) {
            userName = "User";
        }

        binding.tvGreeting.setText(String.format(Locale.US, "Good morning,\n%s! 👋", userName));
        binding.tvStudentInfo.setText("BSIS 2-B | TCU Student");
        binding.tvStreakDays.setText("7 Days");

        planList.clear();
        double totalTargetAll = 0;
        double totalSavedAll = 0;

        Cursor cursor = dbHelper.getPlans(userId);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAN_NAME));
                double target = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAN_TARGET));
                String start = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAN_START_DATE));
                String end = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAN_END_DATE));
                String freq = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAN_FREQUENCY));
                double allowance = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAN_ALLOWANCE));
                String priority = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAN_PRIORITY));
                String notes = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAN_NOTES));

                planList.add(new SavingPlan(id, userId, name, target, start, end, freq, allowance, priority, notes));
                totalTargetAll += target;
                totalSavedAll += dbHelper.getTotalSavedForPlan(id);
            } while (cursor.moveToNext());
            cursor.close();
        }

        binding.tvTotalSavings.setText(String.format(Locale.US, "₱ %.2f", totalSavedAll));
        binding.tvTotalTarget.setText(String.format(Locale.US, "₱ %.2f", totalTargetAll));

        double overallPercent = (totalTargetAll > 0) ? (totalSavedAll / totalTargetAll) * 100 : 0;
        binding.tvTotalProgressPercent.setText(String.format(Locale.US, "%.0f%%", overallPercent));
        binding.totalProgressCircular.setProgress((int) overallPercent);

        adapter.notifyDataSetChanged();
    }
}
