package utils;

/**
 * @author 邓梁
 * @date 2019/12/10 22:39
 * @email 18221221@bjtu.edu.cn
 * 工厂模式接口
 */
public interface SignOperation {
    void op();
    void setData(String data);
    void setIO(IO io);
}
