/*
package com.example.prm391_project_apprestaurants.controllers.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.dal.UserDBContext;
import com.google.android.material.button.MaterialButton;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUsername, edtEmail, edtPassword, edtConfirmPassword;
    MaterialButton btnRegister;
    UserDBContext dbHelper;  // lớp SQLite bạn đã dùng

    // Tạo mã xác nhận 6 số
    int verificationCode = new Random().nextInt(900000) + 100000;

      // Gửi mã này tới email (ở bước sau), hoặc giả lập hiện ra Toast


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // hoặc R.layout.activity_register nếu bạn đặt tên vậy

        Log.d("VERIFICATION_CODE", "Mã xác nhận gửi tới email: " + verificationCode);
        // Ánh xạ
        edtUsername = findViewById(R.id.edtRegisterUsername);
        edtEmail = findViewById(R.id.edtRegisterEmail);
        edtPassword = findViewById(R.id.edtRegisterPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        dbHelper = new UserDBContext(this);

        btnRegister.setOnClickListener(view -> {
            String username = edtUsername.getText().toString();
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.insertUser(username, email, password);
            if (success) {
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                // Điều hướng sang LoginActivity
                Intent intent = new Intent(RegisterActivity.this, Login.class);
                startActivity(intent);
                finish(); // quay về login
            } else {
                Toast.makeText(this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
*/
package com.example.prm391_project_apprestaurants.controllers.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.dal.UserDBContext;
import com.example.prm391_project_apprestaurants.utils.GmailSender;
import com.google.android.material.button.MaterialButton;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUsername, edtEmail, edtPassword, edtConfirmPassword;
    MaterialButton btnRegister;
    private TextView tvAlreadyHaveAccount;

    // Gmail gửi mã xác thực
    private final String systemEmail = "dungthptquelam@gmail.com";         // TODO: Thay bằng Gmail bạn
    private final String systemAppPassword = "dmae rxah ntnc xbbp";      // TODO: Thay bằng app password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ
        edtUsername = findViewById(R.id.edtRegisterUsername);
        edtEmail = findViewById(R.id.edtRegisterEmail);
        edtPassword = findViewById(R.id.edtRegisterPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvAlreadyHaveAccount = findViewById(R.id.tvAlreadyHaveAccount);



        btnRegister.setOnClickListener(view -> {
            String username = edtUsername.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                return;
            }

          // Kiểm tra trùng username
            UserDBContext db = new UserDBContext(this);
              if (db.checkUserExists(username)) {
                Toast.makeText(this, "Tên người dùng đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert user với IsActive = 0
            boolean insertSuccess = db.insertUserWithInactiveStatus(username, email, password);
            if (!insertSuccess) {
                Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo mã xác thực
            int verificationCode = new Random().nextInt(900000) + 100000;

            // Gửi mail xác thực (Async)
            new Thread(() -> {
                try {
                    String subject = "Xác thực tài khoản ResMap";
                    String body = "Mã xác thực của bạn là: " + verificationCode;

                    GmailSender sender = new GmailSender(systemEmail, systemAppPassword);
                    sender.sendMail(email, subject, body);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Mã xác thực đã được gửi đến email", Toast.LENGTH_SHORT).show();

                        // Điều hướng sang VerifyActivity
                        Intent intent = new Intent(this, VerifyActivity.class);
                        intent.putExtra("code", verificationCode);
                        intent.putExtra("username", username);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();
                    });
                } catch (Exception e) {
                    Log.e("EMAIL_ERROR", "Gửi email thất bại", e);
                    runOnUiThread(() ->
                            Toast.makeText(this, "Không gửi được mã xác thực. Kiểm tra email hoặc mạng!", Toast.LENGTH_LONG).show()
                    );
                }
            }).start();
        });
        tvAlreadyHaveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, Login.class);
            startActivity(intent);
        });
    }
}
