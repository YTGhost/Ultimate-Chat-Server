package utils;

import java.util.StringTokenizer;

/**
 * @author 邓梁
 * @date 2019/12/20 21:19
 * @email 18221221@bjtu.edu.cn
 * 将string转换成Byte的工具类
 */
public class StringToByte {
    private static StringToByte stringToByte = new StringToByte();

    public static StringToByte getStringToByte(){
        return stringToByte;
    }

    public byte[] changeto(StringTokenizer parse){
        StringBuilder temp = new StringBuilder(parse.nextToken());
        while(parse.hasMoreTokens()){
            temp.append(" ");
            temp.append(parse.nextToken());
        }
        String image = temp.toString();
        String[] byteValues = image.substring(1, image.length() - 1).split(",");
        byte[] avatar = new byte[byteValues.length];
        for (int i = 0, len = avatar.length; i < len; i++) {
            avatar[i] = Byte.parseByte(byteValues[i].trim());
        }
        return avatar;
    }
}
