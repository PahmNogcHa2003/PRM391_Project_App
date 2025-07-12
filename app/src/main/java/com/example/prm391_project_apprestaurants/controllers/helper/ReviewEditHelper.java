package com.example.prm391_project_apprestaurants.controllers.helper;
import android.net.Uri;

import com.example.prm391_project_apprestaurants.controllers.adapters.PreviewImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReviewEditHelper {
    private static int editingReviewId = -1;
    private static List<Uri> editingUris = new ArrayList<>();
    private static PreviewImageAdapter adapter;

    public static int getEditingReviewId() { return editingReviewId; }
    public static void setEditingReviewId(int id) { editingReviewId = id; }

    public static List<Uri> getEditingUris() { return editingUris; }
    public static void setEditingUris(List<Uri> uris) { editingUris = uris; }

    public static PreviewImageAdapter getAdapter() { return adapter; }
    public static void setAdapter(PreviewImageAdapter adp) { adapter = adp; }
}


