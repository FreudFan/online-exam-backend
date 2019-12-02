package datasource;

import com.alibaba.fastjson.JSONObject;
import model.Topics;
import org.apache.commons.collections4.MapUtils;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class Dtest {
    public static void main ( String[] args ) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
//        testSQLExcute();
//        testSQLUtil();
//        compareSpeed();
        insertOrUpdate();
    }

    private static void insertOrUpdate() throws NoSuchFieldException, IllegalAccessException {
        Topics topics = new Topics();
        topics.setSubject_id(11165186);
        topics.setTopics_id(12);
        JDBCUtils.insertOrUpdate(topics);
    }

    private static void testSQLExcute() throws SQLException {
        String sql = "SELECT * FROM MENU";
        List<Map<String,Object>> list = JDBCUtils.queryForList(sql);
        for ( Map<String,Object> map: list ) {
            System.out.println(new JSONObject(map).toJSONString());
        }
    }

    private static void testSQLUtil() throws SQLException {
        ConnectionManager.getConnection();
        for (int i = 0; i < 100000; i++) {
            String sql = "SELECT * FROM MENU";
//            List<Map<String,Object>> list = JDBCUtils.queryForList(sql);
//            System.out.println(list.toString());
            ResultSet resultSet = JDBCUtils.queryForResultSet(sql);
            System.out.println(resultSet.toString());
        }
    }

    private static void testConnection(){
        //超过最大限制或报"TimeoutException",每打开一个关闭一个就不会发生异常
        for (int i = 0; i < 100000; i++) {
            Connection connection = ConnectionManager.getConnection();
            System.out.println(connection.toString() + "\n------------------------------------");
            ConnectionManager.closeAll(connection, null, null);
        }
    }

    private static void compareSpeed() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM MENU";
        int count = 10000;    //一万  测试了100w和10w，执行jdbc时直接抛异常
        long startTime;
        long endTime;
        System.out.println("开始执行jdbc");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            jdbc(sql);
        }
        endTime = System.currentTimeMillis();
        System.out.println("执行结束");
        long jdbcTime = endTime-startTime;

        System.out.println("开始执行JDBCUtils.queryForResultSet");
        startTime = System.currentTimeMillis(); // 获取开始时间
        for (int i = 0; i < count; i++) {
            JDBCUtils.queryForResultSet(sql);
        }
        endTime = System.currentTimeMillis(); // 获取结束时间
        System.out.println("执行结束");
        long druidTime = endTime-startTime;

        System.out.println("执行一万次查询");
        System.out.println("未使用数据池所用时间：" + jdbcTime);
        System.out.println("使用数据池所用时间：" + druidTime);
    }

    private static void jdbc(String sql) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/eladmin?useUnicode=true&characterEncoding=utf8&useSSL=true";
        String username = "root";
        String password = "d123456";
        Connection conn = DriverManager.getConnection(url,username,password);
        // 获得Statement对象
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery(sql);
        conn.close();
        statement.close();
        rs.close();
    }

}
