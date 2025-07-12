package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.activities.ReviewActivity;
import com.example.prm391_project_apprestaurants.controllers.helper.ReviewEditHelper;
import com.example.prm391_project_apprestaurants.entities.Review;
import com.example.prm391_project_apprestaurants.services.ReviewService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<Review> reviews;
    private final int currentUserId;
    private final ReviewService service;
    private final Context context;

    public ReviewAdapter(Context context, List<Review> reviews, int currentUserId, ReviewService service) {
        this.reviews = reviews;
        this.currentUserId = currentUserId;
        this.service = service;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.tvReviewer.setText(review.getUserName());
        holder.tvContent.setText(review.getContent());
        holder.tvCreatedAt.setText(review.getCreatedAt());
        holder.rbRatingDisplay.setRating(review.getRating());

        if (review.getUserId() == currentUserId) {
            holder.layoutActions.setVisibility(View.VISIBLE);
        } else {
            holder.layoutActions.setVisibility(View.GONE);
        }

        holder.rvReviewImages.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        );

        ImageAdapter adapter = new ImageAdapter(review.getMediaUrls(), context);
        holder.rvReviewImages.setAdapter(adapter);

        holder.btnEdit.setOnClickListener(v -> {
            View editView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_review, null);
            EditText edtContent = editView.findViewById(R.id.edtEditReviewContent);
            RatingBar ratingBar = editView.findViewById(R.id.rbEditRating);
            RecyclerView rvImagePreview = editView.findViewById(R.id.rvEditImagePreview);
            Button btnChooseImage = editView.findViewById(R.id.btnEditChooseImage);

            edtContent.setText(review.getContent());
            ratingBar.setRating(review.getRating());

            List<Uri> selectedImageUris = new ArrayList<>();
            PreviewImageAdapter previewAdapter = new PreviewImageAdapter(context, selectedImageUris);
            rvImagePreview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            rvImagePreview.setAdapter(previewAdapter);

            // Lưu trạng thái để ReviewActivity xử lý
            ReviewEditHelper.setEditingReviewId(review.getId());
            ReviewEditHelper.setEditingUris(selectedImageUris);
            ReviewEditHelper.setAdapter(previewAdapter);

            btnChooseImage.setOnClickListener(view -> {
                ReviewEditHelper.setEditingReviewId(review.getId());
                ReviewEditHelper.setEditingUris(selectedImageUris);
                ReviewEditHelper.setAdapter(previewAdapter);

                if (context instanceof com.example.prm391_project_apprestaurants.controllers.activities.ReviewActivity) {
                    ((com.example.prm391_project_apprestaurants.controllers.activities.ReviewActivity) context).openEditGallery();
                }
            });


            new AlertDialog.Builder(context)
                    .setTitle("Sửa đánh giá")
                    .setView(editView)
                    .setPositiveButton("Lưu", (dialog, which) -> {
                        String newContent = edtContent.getText().toString();
                        int newRating = (int) ratingBar.getRating();

                        // Cập nhật đánh giá
                        service.updateReview(review.getId(), newContent, newRating);

                        // Xóa ảnh cũ
                        service.deleteAllMedia(review.getId());

                        // Thêm ảnh mới (chứa đường dẫn uri)
                        for (Uri uri : ReviewEditHelper.getEditingUris()) {
                            String localPath = ((com.example.prm391_project_apprestaurants.controllers.activities.ReviewActivity) context)
                                    .copyUriToInternalStorage(uri);
                            if (localPath != null) {
                                service.addMedia(review.getId(), localPath);
                            }
                        }


                        // Cập nhật UI
                        review.setContent(newContent);
                        review.setRating(newRating);
                        review.setMediaUrls(service.getMediaUrls(review.getId()));
                        notifyItemChanged(position);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa đánh giá?")
                    .setMessage("Bạn có chắc muốn xóa đánh giá này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        service.deleteReview(review.getId());
                        reviews.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public RatingBar rbRatingDisplay;
        TextView tvReviewer, tvContent, tvCreatedAt;
        Button btnEdit, btnDelete;
        LinearLayout layoutActions;
        public RecyclerView rvReviewImages;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReviewer = itemView.findViewById(R.id.tvReviewer);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            layoutActions = itemView.findViewById(R.id.layoutActions);
            rbRatingDisplay = itemView.findViewById(R.id.rbRatingDisplay);
            rvReviewImages = itemView.findViewById(R.id.rvReviewImages);
        }
    }
}
