package com.ayvytr.customview.util;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

/**
 * Drawable相关操作
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.2.0
 */
public class DrawableUtil {
    private DrawableUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * 设置Drawable Bounds
     *
     * @param drawable 目标Drawable
     * @see Drawable#setBounds(int, int, int, int)
     */
    public static void setBounds(@NonNull Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }
}
