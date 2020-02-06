package utils.factory;

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
 * @date 2019/12/23 18:32
 * @email 18221221@bjtu.edu.cn
 */
public class Change implements SignOperation {

    private String data;
    private IO io;
    private Database database = Database.getDatabase();
    private Connection connection = database.connection;
    private PreparedStatement preparedStatement = database.preparedStatement;

    @Override
    public void op() {
        StringTokenizer parse = new StringTokenizer(data, " ");
        String sign = parse.nextToken();
        String fromAccount = parse.nextToken();
        String toAccount = parse.nextToken();
        String sql = null;
        if (sign.equals("D")){
            // 删除好友
            sql = "select user_id from friendinfo where user_id = ? and friend_id = ?";
            try {
                ResultSet resultSet = database.execResult(sql, fromAccount, toAccount);
                if (resultSet.next()){
                    sql = "delete from friendinfo where user_id = ? and friend_id = ?";
                }else{
                    sql = "delete from friendinfo where friend_id = ? and user_id = ?";
                }
                database.exec(sql, fromAccount, toAccount);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return;
        }
        String group = parse.nextToken();
        sql = "select user_id from friendinfo where user_id = ? and friend_id = ?";
        try {
            ResultSet resultSet = database.execResult(sql, fromAccount, toAccount);
            if (resultSet.next()){
                sql = "update friendinfo set user_group = ? where user_id = ? and friend_id = ?";
            }else {
                sql = "update friendinfo set friend_group = ? where friend_id = ? and user_id = ?";
            }
            database.exec(sql, group, fromAccount, toAccount);
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
