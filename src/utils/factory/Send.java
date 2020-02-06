package utils.factory;

import utils.ChatManager;
import utils.Database;
import utils.IO;
import utils.SignOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * @author 邓梁
 * @date 2019/12/23 23:23
 * @email 18221221@bjtu.edu.cn
 */
public class Send implements SignOperation {

    private String data;
    private IO io;
    private Database database = Database.getDatabase();
    private Connection connection = database.connection;
    private PreparedStatement preparedStatement = database.preparedStatement;

    @Override
    public void op() {
        StringTokenizer parse = new StringTokenizer(data, " ");
        String sign = parse.nextToken();
        String accountFrom = parse.nextToken();
        String accountTo = parse.nextToken();
        StringBuilder temp = new StringBuilder(parse.nextToken());
        while(parse.hasMoreTokens()){
            temp.append(" ");
            temp.append(parse.nextToken());
        }
        String message = temp.toString();
        String sql = null;
        int status = 0;
        // 查询对方是否在线
        sql = "select status from userinfo where account = ?";
        try {
            ResultSet resultSet = database.execResult(sql, accountTo);
            resultSet.next();
            status = resultSet.getInt("status");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (status == 0){
            // 离线时
            if (sign.equals("T")){
                message = "*T" + message;
                sql = "update offline_message set message = concat(message, ?) where from_account = ? and to_account = ?";
                try {
                    database.exec(sql, message, accountFrom, accountTo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }else {
                message = "*I" + message;
                sql = "update offline_message set message = concat(message, ?) where from_account = ? and to_account = ?";
                try {
                    database.exec(sql, message, accountFrom, accountTo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }else{
            // 在线时
            if (sign.equals("T")){
                ChatManager.getChatManager().sendMessage(accountTo, "#SEND#" + "T" + " " + accountFrom + " " + message);

            }else {
                ChatManager.getChatManager().sendMessage(accountTo, "#SEND#" + "I" + " " + accountFrom + " " + message);
            }
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
