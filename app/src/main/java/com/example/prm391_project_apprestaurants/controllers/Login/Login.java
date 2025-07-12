package com.example.prm391_project_apprestaurants.controllers.Login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.user.UserHomeActivity;
import com.example.prm391_project_apprestaurants.dal.UserDBContext;
import com.example.prm391_project_apprestaurants.entities.User;
import com.google.android.material.button.MaterialButton;
import com.example.prm391_project_apprestaurants.controllers.admin.RestaurantManagementActivity;


public class Login extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private MaterialButton btnLogin;
    private UserDBContext userDBContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkAndRequestNotificationPermission();

        // Gọi khởi tạo DbContext để database và user mẫu được tạo (nếu chưa có)
        userDBContext = new UserDBContext(this);

        // Ánh xạ UI
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Xử lý sự kiện login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                UserDBContext db = new UserDBContext(Login.this);
                User user = db.login(username, password);

                if (user != null) {
                    // Lưu thông tin user vào SharedPreferences
                    getSharedPreferences("AppPrefs", MODE_PRIVATE)
                            .edit()
                            .putInt("userId", user.getId())
                            .putString("userName", user.getUsername())
                            .apply();

                    if ("Admin".equalsIgnoreCase(user.getRole())) {
                        Intent intent = new Intent(Login.this, RestaurantManagementActivity.class);
                        startActivity(intent);
                    } else if ("User".equalsIgnoreCase(user.getRole())) {
                        Intent intent = new Intent(Login.this, UserHomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Role không hợp lệ", Toast.LENGTH_SHORT).show();
                    }

            } else {
                    Toast.makeText(Login.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                // Người dùng từ chối và check có nên hiện lại dialog không
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Cần cấp quyền thông báo")
                            .setMessage("Vui lòng vào Cài đặt > Ứng dụng > " + getString(R.string.app_name) + " > Thông báo để bật lại quyền.")
                            .setPositiveButton("Mở cài đặt", (dialog, which) -> {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                }
            }
        }
    }
}