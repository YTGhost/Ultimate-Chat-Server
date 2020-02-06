package utils.factory;

import utils.Database;
import utils.IO;
import utils.SignOperation;
import utils.StringToByte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

/**
 * @author 邓梁
 * @date 2019/12/21 15:23
 * @email 18221221@bjtu.edu.cn
 */
public class SaveInfo implements SignOperation {

    private String data;
    private IO io;
    private Database database = Database.getDatabase();
    private Connection connection = database.connection;
    private PreparedStatement preparedStatement = database.preparedStatement;

    @Override
    public void op() {
        StringTokenizer parse = new StringTokenizer(data, " ");
        String sign = parse.nextToken();
        if (sign.equals("B")){
            String fromAccount = parse.nextToken();
            String toAccount = parse.nextToken();
            String nickname = parse.nextToken();

            String sql = null;
            sql = "select user_id from friendinfo where user_id = ? and friend_id = ?";
            try {
                ResultSet resultSet = database.execResult(sql, fromAccount, toAccount);
                if (resultSet.next()){
                    sql = "update friendinfo set user_nickname = ? where user_id = ? and friend_id = ?";
                }else {
                    sql = "update friendinfo set friend_nickname = ? where friend_id = ? and user_id = ?";
                }
                database.exec(sql, nickname, fromAccount, toAccount);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }
        String account = parse.nextToken();
        String name = parse.nextToken();
        int age = Integer.parseInt(parse.nextToken());
        int sex = Integer.parseInt(parse.nextToken());
        String address = parse.nextToken();
        String birthday = parse.nextToken();
        String sql = null;
        sql = "update userinfo set name = ?, address = ?, birthday = ? where account = ?";
        try {
            database.exec(sql, name, address, birthday, account);
            sql = "update userinfo set age = ?, sex = ? where account = ?";
            database.updateInt(sql, account, age, sex);
            if (sign.equals("Y")){
                byte[] avatar = StringToByte.getStringToByte().changeto(parse);
                sql = "update userinfo set avatar = ? where account = ?";
                database.updateImage(sql, avatar, account);
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
