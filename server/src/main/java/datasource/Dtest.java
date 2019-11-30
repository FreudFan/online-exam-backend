package datasource;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.MapUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Dtest {
    public static void main ( String[] args ) throws SQLException {
        testSQLExcute();
    }

    private static void testSQLExcute() throws SQLException {
        String sql = "SELECT * FROM MENU";
        List<Map<String,Object>> list = JDBCUtils.queryForList(sql);
        for ( Map<String,Object> map: list ) {
            System.out.println(new JSONObject(map).toJSONString());
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

}
