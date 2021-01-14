package cn.rygel.gd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class InputStreamUtils {
    public static String readTextFromInputStream(InputStream is) {
        try {
            InputStreamReader reader = new InputStreamReader(is);
            StringWriter writer = new StringWriter();
            char[] buffer = new char[8 * 1024];
            int chars = reader.read(buffer);
            while (chars >= 0) {
                writer.write(buffer, 0, chars);
                chars = reader.read(buffer);
            }
            return writer.toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return "";
    }
}
