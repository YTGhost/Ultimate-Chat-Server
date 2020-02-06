package utils.factory;

import org.mindrot.jbcrypt.BCrypt;
import utils.ChatManager;
import utils.Database;
import utils.IO;
import utils.SignOperation;

import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * @author 邓梁
 * @date 2019/12/10 22:43
 * @email 18221221@bjtu.edu.cn
 */
public class Login implements SignOperation {
    private String data;
    private IO io;
    private Database database = Database.getDatabase();
    private Connection connection = database.connection;
    private PreparedStatement preparedStatement = database.preparedStatement;

    @Override
    public void op() {
        StringTokenizer parse = new StringTokenizer(data, " ");

        String account = parse.nextToken();
        String password = parse.nextToken();
        String status = parse.nextToken();
        int age;
        int sex;
        byte[] avatar;
        String name;
        String address;
        String telephone;
        String birthday;

        String sql = "select * from userinfo where account = ?";
        try {
            ResultSet resultSet = database.execResult(sql, account);
            if (!resultSet.next()){
                io.printStream.println("User verification Failed");
                return;
            }

            if (BCrypt.checkpw(password, resultSet.getString("password"))){
                if (resultSet.getInt("status") != 0 ){
                    io.printStream.println("User is online");
                    return;
                }

                // 将用户连接加到map中
                ChatManager.getChatManager().add(account, io);

//                 改变用户当前在线状态
                sql = "UPDATE userinfo SET status = ? WHERE account = ?";
                database.updateInt(sql, account, Integer.parseInt(status));


                // 发送验证成功
                io.printStream.println("User verification Passed");

                // 发送用户所需的所有信息
                // 先发送用户的个人信息
                age = resultSet.getInt("age");
                sex = resultSet.getInt("sex");
                avatar = resultSet.getBytes("avatar");
                name = resultSet.getString("name");
                address = resultSet.getString("address");
                telephone = resultSet.getString("telephone");
                birthday  = resultSet.getString("birthday");

                io.printStream.println(age + " " + sex + " " + name + " " + address + " " + telephone + " " + birthday + " " + Arrays.toString(avatar));


                // 传递好友信息
                sql = "select friend_id, user_group, user_nickname from friendinfo where user_id = ?";
                resultSet = database.execResult(sql, account);
                while (resultSet.next()){
                    String friendAccount = resultSet.getString("friend_id");
                    String Group = resultSet.getString("user_group");
                    String nickname = resultSet.getString("user_nickname");

                    sql = "select * from userinfo where account = ?";
                    ResultSet resultSet1 = database.execResult(sql, friendAccount);
                    resultSet1.next();
                    String friendName = resultSet1.getString("name");
                    int friendAge = resultSet1.getInt("age");
                    int friendSex = resultSet1.getInt("sex");
                    byte[] friendAvatar = resultSet1.getBytes("avatar");
                    String friendAddress = resultSet1.getString("address");
                    int friendStatus = resultSet1.getInt("status");
                    String friendTelephone = resultSet1.getString("telephone");
                    String friendBirthday = resultSet1.getString("birthday");

                    io.printStream.println(friendAccount + " " + friendName + " " + friendAge + " " + friendSex +
                            " " + friendAddress + " " + friendStatus + " " + friendTelephone + " " +
                            friendBirthday + " " + Group + " " + nickname + " " + Arrays.toString(friendAvatar));

                    // 发送在线状态给好友
                    if (friendStatus != 0){
                        ChatManager.getChatManager().sendMessage(friendAccount, "#STATUS#" + account + " " + status);
                    }
                }

                sql = "select user_id, friend_group, friend_nickname from friendinfo where friend_id = ?";
                resultSet = database.execResult(sql, account);
                while (resultSet.next()){
                    String friendAccount = resultSet.getString("user_id");
                    String Group = resultSet.getString("friend_group");
                    String nickname = resultSet.getString("friend_nickname");

                    sql = "select * from userinfo where account = ?";
                    ResultSet resultSet1 = database.execResult(sql, friendAccount);
                    resultSet1.next();
                    String friendName = resultSet1.getString("name");
                    int friendAge = resultSet1.getInt("age");
                    int friendSex = resultSet1.getInt("sex");
                    byte[] friendAvatar = resultSet1.getBytes("avatar");
                    String friendAddress = resultSet1.getString("address");
                    int friendStatus = resultSet1.getInt("status");
                    String friendTelephone = resultSet1.getString("telephone");
                    String friendBirthday = resultSet1.getString("birthday");

                    io.printStream.println(friendAccount + " " + friendName + " " + friendAge + " " + friendSex +
                            " " + friendAddress + " " + friendStatus + " " + friendTelephone + " " +
                            friendBirthday + " " + Group + " " + nickname + " " + Arrays.toString(friendAvatar));

                    // 发送在线状态给好友
                    if (friendStatus != 0){
                        ChatManager.getChatManager().sendMessage(friendAccount, "#STATUS#" + account + " " + status);
                    }
                }
                io.printStream.println("**OVER**");

                // 发送离线信息
                sql = "select message, from_account from offline_message where to_account = ?";
                resultSet = database.execResult(sql, account);
                while (resultSet.next()){
                    String messageSet = resultSet.getString("message");
                    if (messageSet == null){
                        continue;
                    }
                    String accountFrom = resultSet.getString("from_account");
                    parse = new StringTokenizer(messageSet, "*");
                    while (parse.hasMoreTokens()){
                        String sentence = parse.nextToken();
                        String sign = sentence.substring(0,1);
                        String message = sentence.substring(1);
                        if (sign.equals("T")){
                            ChatManager.getChatManager().sendMessage(account, "#SEND#" + "T" + " " + accountFrom + " " + message);
                        }else{
                            ChatManager.getChatManager().sendMessage(account, "#SEND#" + "I" + " " + accountFrom + " " + message);
                        }
                    }
                    sql = "update offline_message set message = ? where from_account = ? and to_account = ?";
                    database.exec(sql, null, accountFrom, account);
                }
                // 发送离线好友请求
                sql = "select apply from userinfo where account = ?";
                resultSet = database.execResult(sql, account);
                while (resultSet.next()){
                    String messageSet = resultSet.getString("apply");
                    if (messageSet == null){
                        continue;
                    }
                    parse = new StringTokenizer(messageSet, "*");
                    while (parse.hasMoreTokens()){
                        String message = parse.nextToken();
                        ChatManager.getChatManager().sendMessage(account, message);
                    }
                    sql = "update userinfo set apply = ? where account = ?";
                    database.exec(sql, null, account);
                }
            }else{
                io.printStream.println("User verification Failed");
                return;
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
