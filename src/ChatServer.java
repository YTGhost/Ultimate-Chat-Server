import utils.Database;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 邓梁
 * @date 2019/12/9 18:25
 * @email 18221221@bjtu.edu.cn
 * 主线程
 */
public class ChatServer {
    public static void main(String[] args) {
        Database database = Database.getDatabase();
        database.connection();
        System.out.println("连接数据库成功！");

        try {
            ServerSocket serverSocket = new ServerSocket(30000);
            System.out.println("chatServer端已正常开启，并在30000端口监听中...");

            while (true){
                Socket s = serverSocket.accept();
                new Thread(new ChatThread(s)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
