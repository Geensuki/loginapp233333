package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        int existingUserId = getSharedPreferences("SavingslyPrefs", MODE_PRIVATE).getInt("userId", -1);
        if (existingUserId != -1) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        binding.btnLogin.setOnClickListener(v -> {
            if (binding.etStudentId.getText() == null || binding.etLoginPassword.getText() == null) return;
            String studentId = binding.etStudentId.getText().toString().trim();
            String password = binding.etLoginPassword.getText().toString().trim();

            if (studentId.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                int userId = dbHelper.getUserId(studentId, password);
                if (userId != -1) {
                    if (binding.cbRemember.isChecked()) {
                        getSharedPreferences("SavingslyPrefs", MODE_PRIVATE)
                                .edit()
                                .putInt("userId", userId)
                                .putBoolean("rememberMe", true)
                                .apply();
                    } else {
                        getSharedPreferences("SavingslyPrefs", MODE_PRIVATE)
                                .edit()
                                .putInt("userId", userId)
                                .apply();
                    }

                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.tvForgot.setOnClickListener(v -> showForgotPasswordDialog());

        binding.tvSignupLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void showForgotPasswordDialog() {
        com.google.android.material.bottomsheet.BottomSheetDialog dialog = new com.google.android.material.bottomsheet.BottomSheetDialog(this);
        android.view.View view = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
        dialog.setContentView(view);

        com.google.android.material.textfield.TextInputEditText etEmail = view.findViewById(R.id.et_reset_email);
        com.google.android.material.textfield.TextInputEditText etNewPass = view.findViewById(R.id.et_reset_new_password);
        com.google.android.material.textfield.TextInputEditText etConfirmPass = view.findViewById(R.id.et_reset_confirm_password);

        view.findViewById(R.id.btn_reset_cancel).setOnClickListener(v -> dialog.dismiss());

        view.findViewById(R.id.btn_reset_submit).setOnClickListener(v -> {
            if (etEmail.getText() == null || etNewPass.getText() == null || etConfirmPass.getText() == null) return;

            String email = etEmail.getText().toString().trim();
            String newPass = etNewPass.getText().toString().trim();
            String confirmPass = etConfirmPass.getText().toString().trim();

            if (email.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.resetPassword(email, newPass);
            if (success) {
                Toast.makeText(this, "Password updated successfully! Please log in.", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Account not found with provided Email/Name", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
