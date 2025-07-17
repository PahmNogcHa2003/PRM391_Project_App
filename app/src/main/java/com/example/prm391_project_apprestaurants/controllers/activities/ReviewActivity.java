package com.example.prm391_project_apprestaurants.controllers.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.PreviewImageAdapter;
import com.example.prm391_project_apprestaurants.controllers.adapters.ReviewAdapter;
import com.example.prm391_project_apprestaurants.controllers.helper.ReviewEditHelper;
import com.example.prm391_project_apprestaurants.dal.ReviewDBContext;
import com.example.prm391_project_apprestaurants.dal.ReviewMediaDBContext;
import com.example.prm391_project_apprestaurants.entities.Review;
import com.example.prm391_project_apprestaurants.services.ReviewService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private EditText edtReviewContent;
    private RatingBar ratingBar;
    private Button btnSubmitReview;
    private ImageButton btnChooseImage;
    private RecyclerView rvReviews, rvImagePreview;

    private ReviewDBContext reviewDBContext;
    private ReviewMediaDBContext reviewMediaDBContext;
    private ReviewService reviewService;
    private ReviewAdapter reviewAdapter;
    private PreviewImageAdapter previewImageAdapter;

    private int restaurantId;
    private int currentUserId;

    private List<Uri> selectedImageUris = new ArrayList<>();

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> imageEditPickerLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentUserId = sharedPref.getInt("userId", -1);
        if (currentUserId == -1) {
            Toast.makeText(this, "Không tìm thấy người dùng đăng nhập.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        reviewDBContext = new ReviewDBContext(this);
        reviewMediaDBContext = new ReviewMediaDBContext(this);
        reviewService = new ReviewService(this);
        restaurantId = getIntent().getIntExtra("RESTAURANT_ID", -1);

        initViews();
        initImagePickers();
        setupReviewList();
        setupImagePreview();
    }

    private void initViews() {
        edtReviewContent = findViewById(R.id.etReviewContent);
        ratingBar = findViewById(R.id.rbRating);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        rvReviews = findViewById(R.id.rvReviews);
        rvImagePreview = findViewById(R.id.rvImagePreview);

        btnChooseImage.setOnClickListener(v -> openGallery());

        btnSubmitReview.setOnClickListener(v -> {
            String content = edtReviewContent.getText().toString().trim();
            int rating = (int) ratingBar.getRating();

            if (content.isEmpty() || rating == 0) {
                Toast.makeText(this, "Vui lòng nhập nội dung và chọn số sao.", Toast.LENGTH_SHORT).show();
                return;
            }

            reviewDBContext.addReview(currentUserId, restaurantId, content, rating);
            int lastReviewId = reviewDBContext.getLastInsertedReviewId();

            for (Uri uri : selectedImageUris) {
                String localPath = copyUriToInternalStorage(uri);
                if (localPath != null) {
                    reviewMediaDBContext.addMedia(lastReviewId, localPath);
                }
            }

            Toast.makeText(this, "Đánh giá đã được gửi.", Toast.LENGTH_SHORT).show();

            edtReviewContent.setText("");
            ratingBar.setRating(0);
            selectedImageUris.clear();
            previewImageAdapter.notifyDataSetChanged();
            rvImagePreview.setVisibility(View.GONE);

            // QUAN TRỌNG: báo kết quả OK về cho Activity trước đó
            setResult(RESULT_OK);
            finish();
        });
    }

    private void initImagePickers() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUris.clear();

                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                selectedImageUris.add(data.getClipData().getItemAt(i).getUri());
                            }
                        } else if (data.getData() != null) {
                            selectedImageUris.add(data.getData());
                        }

                        previewImageAdapter.notifyDataSetChanged();
                        rvImagePreview.setVisibility(
                                selectedImageUris.isEmpty() ? View.GONE : View.VISIBLE
                        );
                    }
                }
        );

        imageEditPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ReviewEditHelper.getEditingUris().clear();

                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                                ReviewEditHelper.getEditingUris().add(data.getClipData().getItemAt(i).getUri());
                            }
                        } else if (data.getData() != null) {
                            ReviewEditHelper.getEditingUris().add(data.getData());
                        }

                        PreviewImageAdapter adapter = ReviewEditHelper.getAdapter();
                        if (adapter != null) adapter.notifyDataSetChanged();
                    }
                }
        );
    }

    private void setupReviewList() {
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        loadReviews();
    }

    private void setupImagePreview() {
        rvImagePreview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        previewImageAdapter = new PreviewImageAdapter(this, selectedImageUris);
        rvImagePreview.setAdapter(previewImageAdapter);
        rvImagePreview.setVisibility(View.GONE);
    }

    public void openEditGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        imageEditPickerLauncher.launch(Intent.createChooser(intent, "Chọn ảnh mới"));
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Chọn ảnh"));
    }

    private void loadReviews() {
        // Sửa đoạn này: truyền thêm Callback xóa đánh giá để setResult(RESULT_OK)
        List<Review> reviews = reviewService.getReviews(restaurantId);

        reviewAdapter = new ReviewAdapter(
                this,
                reviews,
                currentUserId,
                reviewService,
                // callback khi xóa review thành công:
                () -> {
                    setResult(RESULT_OK); // báo về cho Detail refresh khi trở lại
                }
        );
        rvReviews.setAdapter(reviewAdapter);
    }

    public String copyUriToInternalStorage(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            File file = new File(getFilesDir(), "image_" + System.currentTimeMillis() + ".jpg");
            try (OutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
