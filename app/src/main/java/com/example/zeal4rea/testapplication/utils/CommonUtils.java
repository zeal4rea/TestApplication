package com.example.zeal4rea.testapplication.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.Collection;

public class CommonUtils {
    public static void showToast(Context context, CharSequence cs) {
        showToast(context, cs, Toast.LENGTH_SHORT);
    }
    public static void showToast(Context context, CharSequence cs, int length) {
        Toast.makeText(context, cs, length).show();
    }
    public static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }
}
