package utils;

import java.sql.*;

/**
 * @author 邓梁
 * @date 2019/12/9 20:19
 * @email 18221221@bjtu.edu.cn
 * 数据库工具类
 */
public class Database {
    private static Database database = new Database();
    public Connection connection = null;   // 连接
    public PreparedStatement preparedStatement = null; // 动态查询

    /**
     * 默认构造函数
     */
    private Database() {
    }

    public static Database getDatabase() {
        return database;
    }

    public void connection() {
        try {
            String connectionString = "###";
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet execResult(String sql, String... data) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i < data.length + 1; i++) {
            preparedStatement.setString(i, data[i - 1]);
        }
        return preparedStatement.executeQuery();
    }

    public void exec(String sql, String... data) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i < data.length + 1; i++) {
            preparedStatement.setString(i, data[i - 1]);
        }
        preparedStatement.executeUpdate();
    }

    public void exec(String sql) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
    }

    public void updateImage(String sql, byte[] data, String account) throws SQLException{
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBytes(1, data);
        preparedStatement.setString(2, account);
        preparedStatement.executeUpdate();
    }

    public void updateInt(String sql, String account, int... data) throws SQLException{
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i < data.length + 1; i++) {
            preparedStatement.setInt(i, data[i - 1]);
        }
        preparedStatement.setString(data.length + 1, account);
        preparedStatement.executeUpdate();

    }
}
