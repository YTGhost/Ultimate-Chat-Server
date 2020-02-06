import utils.ChatManager;
import utils.IO;
import utils.SignFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * @author 邓梁
 * @date 2019/12/9 19:29
 * @email 18221221@bjtu.edu.cn
 * 多线程
 */
public class ChatThread implements Runnable {
    private Socket socket;
    private IO io;
    private String ip;

    public ChatThread(Socket s) {
        socket = s;
        io = new IO(s);
        ip = socket.getInetAddress().getHostAddress();
    }

    public void out(String msg) {
        io.printStream.println(msg);
    }

    public String in() {
        String content = null;
        try {
            content = io.bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println("IP为:" + ip + "的用户断开连接");
        }
        return content;
    }

    @Override
    public void run() {
        System.out.println("IP为:" + ip + "的用户连接成功");
        String content = null;
        SignFactory signFactory = new SignFactory();
        while ((content = in()) != null) {
            StringTokenizer parse = new StringTokenizer(content, "#");
            String sign = parse.nextToken();
            String data = parse.nextToken();
            // 测试用
            System.out.println(sign);
            System.out.println(data);
            // 测试用
            signFactory.getOp(sign, data, io).op();
        }

    }
}
