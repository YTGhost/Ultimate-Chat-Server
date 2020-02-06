package utils.factory;

import utils.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

/**
 * @author 邓梁
 * @date 2019/12/11 13:39
 * @email 18221221@bjtu.edu.cn
 */
public class Register implements SignOperation {
    private String data;
    private IO io;
    private Database database = Database.getDatabase();
    private Connection connection = database.connection;
    private PreparedStatement preparedStatement = database.preparedStatement;

    @Override
    public void op() {
        StringTokenizer parse = new StringTokenizer(data, " ");

        String account = null;
        String username = parse.nextToken();
        String password = parse.nextToken();
        String telephone = parse.nextToken();
        String birthday = parse.nextToken();
        int sex = Integer.parseInt(parse.nextToken());
        byte[] avatar = StringToByte.getStringToByte().changeto(parse);

        String sql = "select telephone from userinfo where telephone = ?";
        try {
            ResultSet resultSet = database.execResult(sql, telephone);
            if (resultSet.next()) {
                io.printStream.println("Exist");
                return;
            }
// 临时替换，测试用
            GetMessageCode getMessageCode = new GetMessageCode();
            String code = getMessageCode.getCode(telephone);
//            String code = "123456";
            io.printStream.println("SEND_verification_code" + " " + code);

            String sign = io.bufferedReader.readLine();
            if (sign.equals("Success")) {
                sql = "insert into userinfo (name, password, telephone, birthday)values (?, ?, ?, ?) ";
                database.exec(sql, username, password, telephone, birthday);

                // 得到账号
                sql = "select account from userinfo where telephone = ?";
                resultSet = database.execResult(sql, telephone);
                resultSet.next();
                account = resultSet.getString("account");

                io.printStream.println(account);

                // 上传头像
                sql = "update userinfo set avatar = ? where account = ?";
                database.updateImage(sql, avatar, account);

                // 上传性别
                sql = "update userinfo set sex = ? where account = ?";
                database.updateInt(sql, account, sex);

            } else {
                return;
            }

        } catch (SQLException | IOException e) {
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
