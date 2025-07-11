package com.example.prm391_project_apprestaurants.controllers.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.dal.UserDBContext;
import com.example.prm391_project_apprestaurants.utils.GmailSender;

import java.util.Random;

public class VerifyActivity extends AppCompatActivity {

    EditText edtCode;
    Button btnVerify, btnResendCode;
    TextView tvResentCodeInfo;

    int codeSent;
    String username, email, password;
    Random random = new Random();

    // Gmail hệ thống
    private final String systemEmail = "dungthptquelam@gmail.com";         // Gmail của bạn
    private final String systemAppPassword = "dmae rxah ntnc xbbp";        // App password của bạn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        // Ánh xạ view
        edtCode = findViewById(R.id.edtCode);
        btnVerify = findViewById(R.id.btnVerify);
        btnResendCode = findViewById(R.id.btnResendCode);
        tvResentCodeInfo = findViewById(R.id.tvResentCodeInfo);

        // Nhận dữ liệu từ RegisterActivity
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        codeSent = getIntent().getIntExtra("code", -1);

        Log.d("VERIFY", getString(R.string.resend_log) + codeSent);

        // Nút xác thực
        btnVerify.setOnClickListener(view -> {
            String enteredCodeStr = edtCode.getText().toString().trim();

            if (enteredCodeStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.empty_code), Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int codeEntered = Integer.parseInt(enteredCodeStr);
                if (codeEntered == codeSent) {
                    UserDBContext db = new UserDBContext(this);
                    boolean success = db.activateUserByUsername(username);
                    if (success) {
                        Toast.makeText(this, getString(R.string.verify_success), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, Login.class));
                        finish();
                    } else {
                        Toast.makeText(this, getString(R.string.create_user_fail), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.wrong_code), Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, getString(R.string.invalid_code), Toast.LENGTH_SHORT).show();
            }
        });

        // Nút gửi lại mã
        btnResendCode.setOnClickListener(v -> {
            codeSent = random.nextInt(900000) + 100000;

            String subject = "Mã xác thực đăng ký";
            String message = "Mã xác thực mới của bạn là: " + codeSent;

            new Thread(() -> {
                try {
                    GmailSender sender = new GmailSender(systemEmail, systemAppPassword);
                    sender.sendMail(email, subject, message);

                    runOnUiThread(() -> {
                        Toast.makeText(this, getString(R.string.resend_success), Toast.LENGTH_SHORT).show();
                        tvResentCodeInfo.setText(getString(R.string.code_resend_label) + codeSent);
                    });
                } catch (Exception e) {
                    Log.e("EMAIL", "Gửi email thất bại", e);
                    runOnUiThread(() ->
                            Toast.makeText(this, "Gửi email thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }).start();
        });
    }
}