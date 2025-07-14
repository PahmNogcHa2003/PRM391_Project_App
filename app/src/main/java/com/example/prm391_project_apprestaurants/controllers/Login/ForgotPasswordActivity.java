package com.example.prm391_project_apprestaurants.controllers.Login;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.dal.UserDBContext;
import com.example.prm391_project_apprestaurants.utils.GmailSender;
import com.google.android.material.button.MaterialButton;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edtEmail;

    private EditText edtUsername;
    private MaterialButton btnSendCode;
    private UserDBContext db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        edtEmail = findViewById(R.id.forgotedtRegisterEmail);
        edtUsername = findViewById(R.id.forgotedtRegisterUsername);
        btnSendCode = findViewById(R.id.fotgotbtnRegister);
        db = new UserDBContext(this);

        btnSendCode.setOnClickListener(view -> {
            String email = edtEmail.getText().toString().trim();
            String username = edtUsername.getText().toString().trim();

            if (email.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ email và tên đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }


            // Kiểm tra username + email có khớp không
            boolean isValid = db.checkUsernameEmailMatch(username, email);
            if (!isValid) {
                Toast.makeText(this, "Không tìm thấy tài khoản với email và tên đăng nhập này", Toast.LENGTH_LONG).show();
                return;
            }

            // Sinh mật khẩu mới
            String newPassword = generateRandomPassword();

            // Gửi email
            String subject = "Mật khẩu mới cho tài khoản của bạn";
            String body = "Chào bạn,\n\nĐây là mật khẩu mới của bạn: " + newPassword +
                    "\nHãy dùng mật khẩu này để đăng nhập và đổi lại mật khẩu sau khi vào hệ thống.";

            // Gửi email ở thread riêng
            new Thread(() -> {
                try {
                    GmailSender sender = new GmailSender("dungthptquelam@gmail.com", "dmae rxah ntnc xbbp");
                    sender.sendMail(email, subject, body);

                    // Cập nhật mật khẩu sau khi gửi thành công
                  /*  db.updatePasswordByEmail(email, newPassword);*/

                 db.updatePasswordByUsernameAndEmail(username, email, newPassword);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Đã gửi mật khẩu mới về email!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, Login.class));
                        finish();
                    });
                } catch (Exception e) {
                    Log.e("ForgotPassword", "Gửi email lỗi", e);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Gửi email thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }).start();
        });
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return password.toString();
    }
}
