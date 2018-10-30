package cn.mt.gd.utils;

import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardUtils {
    /**
     * 实现文本复制功能
     * 注意：导包的时候
     * API 11之前： android.text.ClipboardManager
     * API 11之后： android.content.ClipboardManager
     *
     * @param context
     * @param content
     */
    public static void copy(Context context, String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     *
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }
}
