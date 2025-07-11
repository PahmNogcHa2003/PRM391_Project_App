package com.example.prm391_project_apprestaurants.controllers.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.databinding.ActivityUpdateUserDashboardBinding;
import com.example.prm391_project_apprestaurants.entities.User;
import com.example.prm391_project_apprestaurants.services.UserService;
import com.example.prm391_project_apprestaurants.utils.Constant;
import com.example.prm391_project_apprestaurants.utils.GmailSender;
import com.example.prm391_project_apprestaurants.utils.UtilHelper;

import javax.mail.MessagingException;

public class UpdateUserDashboardActivity extends AppCompatActivity {

    private ActivityUpdateUserDashboardBinding binding;
    private UserService userService;
    private User updateUser;
    private GmailSender gmailSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_user_dashboard);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_user_dashboard);
        Initialize();
        BindingData();
        RegisterEvents();
    }

    private void Initialize() {
        userService = new UserService(this);
        gmailSender = new GmailSender(Constant.SENDER_EMAIL, Constant.SENDER_PASSWORD);
    }

    private void BindingData() {
        int userId = getIntent().getIntExtra("userId", 0);
        updateUser = userService.getUserById(userId);
        binding.setViewHolder(updateUser);
    }

    private void RegisterEvents() {
        binding.buttonCancel.setOnClickListener(v -> finish());
        binding.buttonUpdate.setOnClickListener(this::UpdateUser);
        binding.editUsername.setOnFocusChangeListener(this::updateUsername);
        binding.editPassword.setOnFocusChangeListener(this::updatePassword);
    }

    private void UpdateUser(View view) {
        try {
            boolean isUpdateSuccess = userService.updateUser(updateUser);
            if (isUpdateSuccess) {
                gmailSender.sendMail(updateUser.getEmail(), "Update user", "Update user successfully");
                Toast.makeText(this, "Update user successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, UserManagementActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Update user failed", Toast.LENGTH_SHORT).show();
            }
        }catch (MessagingException e) {
            Toast.makeText(this, "Update user failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUsername(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateUser.setUsername(binding.editUsername.getText().toString());
        }
    }

    private void updatePassword(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateUser.setPassword(binding.editPassword.getText().toString());
        }
    }
}