package cn.rygel.gd.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardUtils {

    /**
     * 实现文本复制功能
     *
     * @param context
     * @param content
     */
    public static void copy(Context context, String content) {
        getClipboardManager(context).setText(content.trim());
    }

    public static void copy(Context context,String label,String text){
        ClipData myClip = ClipData.newPlainText(label, text);
        getClipboardManager(context).setPrimaryClip(myClip);
    }

    public static String paste(Context context) {
        ClipData clipData = getClipboardManager(context).getPrimaryClip();
        if(clipData != null && clipData.getItemCount() > 0) {
            return clipData.getItemAt(0).coerceToText(context).toString();
        }
        return null;
    }

    private static ClipboardManager getClipboardManager(Context context){
        return (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

}
