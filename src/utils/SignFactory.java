package utils;

import utils.factory.*;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 邓梁
 * @date 2019/12/10 22:46
 * @email 18221221@bjtu.edu.cn
 * 工厂模式
 */
public class SignFactory {
    private Map<String, SignOperation> signOperationMap = new HashMap<>();

    public SignFactory() {
        signOperationMap.put("LOGIN", new Login());
        signOperationMap.put("REGISTER", new Register());
        signOperationMap.put("LOOKUP", new Lookup());
        signOperationMap.put("SAVEINFO", new SaveInfo());
        signOperationMap.put("STATUS", new Status());
        signOperationMap.put("ADDFRIEND", new AddFriend());
        signOperationMap.put("ACCEPT", new Accept());
        signOperationMap.put("CHANGE", new Change());
        signOperationMap.put("SEND", new Send());
        signOperationMap.put("FORGET", new Forget());
    }

    public SignOperation getOp(String sign, String data, IO io) {
        signOperationMap.get(sign).setIO(io);
        signOperationMap.get(sign).setData(data);
        return signOperationMap.get(sign);
    }
}
