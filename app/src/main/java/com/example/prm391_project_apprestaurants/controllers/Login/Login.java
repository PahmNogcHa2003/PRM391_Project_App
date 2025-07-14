/*
package com.example.prm391_project_apprestaurants.controllers.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.admin.SideBarFragment;
import com.example.prm391_project_apprestaurants.controllers.user.HomeUser;
import com.example.prm391_project_apprestaurants.controllers.viewholders.BindingViewHolder;
import com.example.prm391_project_apprestaurants.dal.UserDBContext;
import com.example.prm391_project_apprestaurants.entities.User;
import com.google.android.material.button.MaterialButton;
import com.example.prm391_project_apprestaurants.controllers.admin.RestaurantManagementActivity;


public class Login extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private MaterialButton btnLogin;
    private UserDBContext userDBContext;
    private TextView txtGoToRegister;

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
        txtGoToRegister = findViewById(R.id.txtGoToRegister); // ánh xạ text đăng ký

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
                        Intent intent = new Intent(Login.this, HomeUser.class); // <- tên class activity cho User
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Role không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Xử lý chuyển sang RegisterActivity khi người dùng chưa có tài khoản
        txtGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegisterActivity.class); // <- tên Activity đăng ký
                startActivity(intent);
            }
        });
    }
}*/
package com.example.prm391_project_apprestaurants.controllers.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.admin.RestaurantManagementActivity;
import com.example.prm391_project_apprestaurants.controllers.user.HomeUser;
import com.example.prm391_project_apprestaurants.dal.UserDBContext;
import com.example.prm391_project_apprestaurants.entities.User;
import com.google.android.material.button.MaterialButton;

public class Login extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private MaterialButton btnLogin;
    private UserDBContext userDBContext;
    private TextView txtGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDBContext = new UserDBContext(this);

        // Ánh xạ UI
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtGoToRegister = findViewById(R.id.txtGoToRegister);
        TextView txtForgotPasswordLogin = findViewById(R.id.txtForgotPasswordLogin);

        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = userDBContext.login(username, password);

            if (user != null) {
                // Kiểm tra role
                if ("Admin".equalsIgnoreCase(user.getRole())) {
                    startActivity(new Intent(Login.this, RestaurantManagementActivity.class));
                } else if ("User".equalsIgnoreCase(user.getRole())) {
                    startActivity(new Intent(Login.this, HomeUser.class));
                } else {
                    Toast.makeText(Login.this, "Quyền người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Không tìm thấy user hoặc chưa xác thực
                Toast.makeText(Login.this, "Sai tài khoản, mật khẩu hoặc tài khoản chưa được xác thực", Toast.LENGTH_LONG).show();
            }
        });

        txtGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, RegisterActivity.class);
            startActivity(intent);
        });
        txtForgotPasswordLogin.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }
}

