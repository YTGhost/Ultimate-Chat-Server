package utils.factory;

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
 * @date 2019/12/19 16:08
 * @email 18221221@bjtu.edu.cn
 */
public class Lookup implements SignOperation {
    private String data;
    private IO io;
    private Database database = Database.getDatabase();
    private Connection connection = database.connection;
    private PreparedStatement preparedStatement = database.preparedStatement;

    @Override
    public void op() {
        StringTokenizer parse = new StringTokenizer(data, " ");
        String sign = parse.nextToken();
        String account = parse.nextToken();
        String sql = null;
        if (sign.equals("F")){
            sql = "select name, avatar, status from userinfo where account = ?";
            try {
                ResultSet resultSet = database.execResult(sql, account);
                if (!resultSet.next()){
                    io.printStream.println("Not Found");
                }else{
                    String name = resultSet.getString("name");
                    int status = resultSet.getInt("status");
                    byte[] avatar = resultSet.getBytes("avatar");
                    io.printStream.println(name + " " + status + " " + Arrays.toString(avatar));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{

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
