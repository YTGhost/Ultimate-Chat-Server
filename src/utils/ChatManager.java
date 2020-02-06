package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 邓梁
 * @date 2019/12/9 20:45
 * @email 18221221@bjtu.edu.cn
 * 全局管理类
 */
public class ChatManager {
    private static ChatManager chatManager = new ChatManager();

    Map<String, IO> map = new HashMap<>(); // 账号映射其对应的IO

    public void add(String account, IO io){
        map.put(account, io);
    }

    public void remove(String account){
        System.out.println(account + "已离线");
        map.remove(account);
    }

    public ChatManager(){

    }

    public static ChatManager getChatManager(){
        return chatManager;
    }

    public void sendMessage(String to, String message){
        for (Map.Entry<String, IO> entry : map.entrySet()){
            IO io = entry.getValue();
            if (entry.getKey().equals(to)){
                io.printStream.println(message);
            }
        }
    }


}
