package utils.factory;

import org.apache.commons.lang.StringUtils;
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
 * @date 2019/12/21 17:57
 * @email 18221221@bjtu.edu.cn
 */
public class AddFriend implements SignOperation {

    private String data;
    private IO io;
    private Database database = Database.getDatabase();
    private Connection connection = database.connection;
    private PreparedStatement preparedStatement = database.preparedStatement;

    @Override
    public void op() {
        StringTokenizer parse = new StringTokenizer(data, " ");
        String accountFrom = parse.nextToken();
        String accountTo = parse.nextToken();
        String nickname = parse.nextToken();
        String group = parse.nextToken();
        StringBuilder temp = new StringBuilder(parse.nextToken());
        while(parse.hasMoreTokens()){
            temp.append(parse.nextToken());
        }
        String message = temp.toString();
        String sql = null;
        String name;
        byte[] avatar;
        int status;

        try {
            //language=TSQL
            sql = "select name, avatar from userinfo where account = ?";
            ResultSet resultSet = database.execResult(sql, accountFrom);
            resultSet.next();
            name = resultSet.getString("name");
            avatar = resultSet.getBytes("avatar");

             //查询对方是否在线
            sql = "select status from userinfo where account = ?";
            resultSet = database.execResult(sql, accountTo);
            resultSet.next();
            status = resultSet.getInt("status");
            if (status == 0){
                //离线消息
                sql = "update userinfo set apply = ? where account = ?";
                database.exec(sql, "*" + "#ADDFRIEND#" + accountFrom + " " + name + " " + nickname + " " + group + " " + message + " " + Arrays.toString(avatar), accountTo);
            }else{
                ChatManager.getChatManager().sendMessage(accountTo, "#ADDFRIEND#" + accountFrom + " " + name + " " + nickname + " " + group + " " + message + " " + Arrays.toString(avatar));
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
