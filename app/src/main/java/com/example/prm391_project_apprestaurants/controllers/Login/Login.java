package com.example.prm391_project_apprestaurants.controllers.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
                    if ("Admin".equalsIgnoreCase(user.getRole())) {
                        Intent intent = new Intent(Login.this, RestaurantManagementActivity.class);
                        startActivity(intent);
                    } else if ("User".equalsIgnoreCase(user.getRole())) {
                        Intent intent = new Intent(Login.this, UserHomeActivity.class); // <- tên class activity cho User
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
}