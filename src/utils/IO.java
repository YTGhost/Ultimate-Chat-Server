package utils;

import java.io.*;
import java.net.Socket;

/**
 * @author 邓梁
 * @date 2019/12/9 20:51
 * @email 18221221@bjtu.edu.cn
 * 输入输出工具类
 */
public class IO {
    public BufferedReader bufferedReader = null;
    public PrintStream printStream = null;
    public ByteArrayInputStream byteArrayInputStream = null;
    public ByteArrayOutputStream byteArrayOutputStream = null;

    public IO(Socket s){
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            printStream = new PrintStream(s.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            bufferedReader.close();
            printStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
