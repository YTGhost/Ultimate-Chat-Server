package utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.JsonObject;

/**
 * @author 邓梁
 * @date 2019/12/15 13:51
 * @email 18221221@bjtu.edu.cn
 * HTTP连接工具类
 */
public class Utility {
    public static void jhson() {
        String jsonString ="{\"name\":\"dengliang\",\"password\":\"ytdl200101230013\",\"email\":\"283304489@qq.com\"}";
        JsonObject json =  new JsonObject();
    }
    public static String getNowDateStr() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(str.getBytes("utf-8"));
            System.out.println(toHex(bytes).toLowerCase());
            return toHex(bytes).toLowerCase();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }
}
