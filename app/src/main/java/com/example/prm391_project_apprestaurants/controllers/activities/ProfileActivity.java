package com.example.prm391_project_apprestaurants.controllers.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.Login.Login;
import com.example.prm391_project_apprestaurants.controllers.user.ChangePasswordActivity;
import com.example.prm391_project_apprestaurants.dal.UserDBContext;
import com.example.prm391_project_apprestaurants.entities.User;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail, tvRole;
    private Button btnChangePassword, btnLogout;
    private UserDBContext userDB;
    private int userId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ view
        tvUsername = findViewById(R.id.tv_profile_username);
        tvEmail = findViewById(R.id.tv_profile_email);

        btnChangePassword = findViewById(R.id.btn_change_password);
        btnLogout = findViewById(R.id.btn_logout);

        // Lấy thông tin user từ SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        userId = sharedPref.getInt("userId", -1);
        userName = sharedPref.getString("userName", "Khách");

        // Khởi tạo DB context
        userDB = new UserDBContext(this);

        // Hiển thị thông tin user
        displayUserInfo();

        // Xử lý nút đổi mật khẩu
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // Xử lý nút đăng xuất
        btnLogout.setOnClickListener(v -> logout());
    }

    private void displayUserInfo() {
        tvUsername.setText(userName);

        // Lấy thông tin từ database - sử dụng getUserById() trả về đối tượng User
        User user = userDB.getUserById(userId);
        if (user != null) {
            tvEmail.setText(user.getEmail()); // Lấy email từ đối tượng User
              // Lấy role từ đối tượng User

            // Nếu cần hiển thị thêm thông tin khác
            // tvSomething.setText(user.getSomething());
        } else {
            Toast.makeText(this, "Không thể tải thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        // Hiển thị hộp thoại xác nhận
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Xóa thông tin đăng nhập
                    SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.clear();
                    editor.apply();

                    // Chuyển về màn hình login
                    Intent intent = new Intent(this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                    // Hiển thị thông báo đã đăng xuất
                    Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Không", (dialog, which) -> {
                    // Đóng hộp thoại và không làm gì cả
                    dialog.dismiss();
                })
                .setCancelable(false) // Ngăn không cho tắt bằng cách nhấn ra ngoài
                .show();
    }
}
