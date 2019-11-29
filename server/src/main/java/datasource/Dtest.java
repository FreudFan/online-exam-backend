package datasource;

import java.sql.Connection;

public class Dtest {
    public static void main ( String[] args ) {
        //超过最大限制或报"TimeoutException",每打开一个关闭一个就不会发生异常
        for (int i = 0; i < 100000; i++) {
            Connection connection = Druid.getConnection();
            System.out.println(connection.toString() + "\n------------------------------------");
            Druid.closeAll(connection, null, null);
        }
    }
}
