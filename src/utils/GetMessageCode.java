package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Random;

/**
 * @author 邓梁
 * @date 2019/12/15 14:02
 * @email 18221221@bjtu.edu.cn
 * 获取验证码和发送短信请求
 */
public class GetMessageCode {
    public GetMessageCode() {

    }

    public static String smsCode() {
        Random random = new Random();
        StringBuilder verification = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            verification.append(random.nextInt(10));
        }
        return verification.toString();
    }

    public String getCode(String phone) {
        // 您的短信账号
        String Account = "###";

        // 您的短信账号密码
        String Password = "###";

        // 是否需要状态报告
        String NeedStatus = "true";
        String code = smsCode();
        String message = String.format("感谢您注册Ultimate-Chat，您的验证码是%s。如非本人操作，请忽略本短信！", code);//短信内容
        String mobile = phone;//要发送的手机号,多个手机号用,隔开
        String ts = Utility.getNowDateStr();//时间戳
        Password = Utility.getMD5(Account + Password + ts);// Md5签名(账号+密码+时间戳)
        HashMap params = new HashMap();
        //请求参数

        params.put("account", Account);
        params.put("pswd", Password);
        params.put("mobile", mobile);
        params.put("msg", message);
        params.put("ts", ts);
        params.put("needstatus", NeedStatus);
        String rep = ZxHttpUtil.sendPostMessage("http://139.196.108.241:8080/Api/HttpSendSMYzm.ashx", params, "UTF-8");
        System.out.println(rep);
        JsonParser parser = new JsonParser();

        JsonObject json = (JsonObject) parser.parse(rep);

        System.out.println("<br> result_msg:" + json.get("result_msg"));
        return code;
    }

    public static void main(String[] args) {
//        测试用
//        GetMessageCode getMessageCode = new GetMessageCode();
//        String code = getMessageCode.getCode("15623836237");
//        System.out.println(code);
    }
}
