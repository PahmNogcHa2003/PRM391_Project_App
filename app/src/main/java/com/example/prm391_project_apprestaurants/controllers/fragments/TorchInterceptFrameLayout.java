package com.example.prm391_project_apprestaurants.controllers.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TorchInterceptFrameLayout extends android.widget.FrameLayout {
    public TorchInterceptFrameLayout(Context context) {
        super(context);
    }

    public TorchInterceptFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
