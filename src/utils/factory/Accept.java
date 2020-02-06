package utils.factory;

import utils.Database;
import utils.IO;
import utils.SignOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;

/**
 * @author 邓梁
 * @date 2019/12/22 15:51
 * @email 18221221@bjtu.edu.cn
 */
public class Accept implements SignOperation {

    private String data;
    private IO io;
    private Database database = Database.getDatabase();
    private Connection connection = database.connection;
    private PreparedStatement preparedStatement = database.preparedStatement;

    @Override
    public void op() {
        StringTokenizer parse = new StringTokenizer(data, " ");
        String userAccount = parse.nextToken();
        String friendAccount = parse.nextToken();
        String userGroup = parse.nextToken();
        String friendGroup = parse.nextToken();
        String userNickName = parse.nextToken();
        String friendNickName = parse.nextToken();

        String sql = "insert into friendinfo (user_id, friend_id, user_group, friend_group, user_nickname, friend_nickname)values (?, ?, ?, ?, ?, ?)";

        try {
            database.exec(sql, userAccount, friendAccount, userGroup, friendGroup, userNickName, friendNickName);

            sql = "insert into offline_message (from_account, to_account)values (?, ?)";
            database.exec(sql, userAccount, friendAccount);
            database.exec(sql, friendAccount, userAccount);
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
