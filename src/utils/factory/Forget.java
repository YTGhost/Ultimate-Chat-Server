package utils.factory;

import org.mindrot.jbcrypt.BCrypt;
import utils.Database;
import utils.GetMessageCode;
import utils.IO;
import utils.SignOperation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

/**
 * @author 邓梁
 * @date 2019/12/24 14:52
 * @email 18221221@bjtu.edu.cn
 */
public class Forget implements SignOperation {

    private String data;
    private IO io;
    private Database database = Database.getDatabase();
    private Connection connection = database.connection;
    private PreparedStatement preparedStatement = database.preparedStatement;

    @Override
    public void op() {
        String telephone = data;

        String sql = null;
        try {
            sql = "select account from userinfo where telephone = ?";
            ResultSet resultSet = database.execResult(sql, telephone);
            if (!resultSet.next()){
                io.printStream.println("Not Found");
                return;
            }
            GetMessageCode getMessageCode = new GetMessageCode();
            String code = getMessageCode.getCode(telephone);
//            String code = "123456";
            io.printStream.println(code);

            String update = null;
            try {
                update = io.bufferedReader.readLine();
                if (update.equals("Verify failed")){
                    return;
                }
                sql = "update userinfo set password = ? where telephone = ?";
                database.exec(sql, BCrypt.hashpw(update, BCrypt.gensalt()), telephone);
            } catch (IOException e) {
                e.printStackTrace();
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
