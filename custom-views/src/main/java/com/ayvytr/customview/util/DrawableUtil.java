package com.ayvytr.customview.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * @author admin
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
