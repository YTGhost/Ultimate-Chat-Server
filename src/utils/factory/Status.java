package utils.factory;

import utils.ChatManager;
import utils.Database;
import utils.IO;
import utils.SignOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

/**
 * @author 邓梁
 * @date 2019/12/21 16:35
 * @email 18221221@bjtu.edu.cn
 */
public class Status implements SignOperation {

    private String data;
    private IO io;
    private Database database = Database.getDatabase();
    private Connection connection = database.connection;
    private PreparedStatement preparedStatement = database.preparedStatement;

    @Override
    public void op() {
        StringTokenizer parse = new StringTokenizer(data, " ");
        int status = Integer.parseInt(parse.nextToken());
        String account = parse.nextToken();
        if (status == 0){
            ChatManager.getChatManager().remove(account);
        }
        String sql = "update userinfo set status = ? where account = ?";
        try {
            database.updateInt(sql, account, status);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 发送提醒给用户好友，待施工标记
        try {
            sql = "select friend_id from friendinfo where user_id = ?";
            ResultSet resultSet = database.execResult(sql, account);
            while (resultSet.next()){
                String friendAccount = resultSet.getString("friend_id");
                ChatManager.getChatManager().sendMessage(friendAccount, "#STATUS#" + account + " " + status);
            }
            sql = "select user_id from friendinfo where friend_id = ?";
            resultSet = database.execResult(sql, account);
            while (resultSet.next()){
                String friendAccount = resultSet.getString("user_id");
                ChatManager.getChatManager().sendMessage(friendAccount, "#STATUS#" + account + " " + status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void setIO(IO io) {
        this.io = io;
    }
}
