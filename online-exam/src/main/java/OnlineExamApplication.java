import datasource.ConnectionManager;
import lombok.extern.slf4j.Slf4j;
import server.Tomcat8;

@Slf4j
public class OnlineExamApplication {
    public static void main(String[] args) {
        Thread tomcat8 = new Thread(new Tomcat8());
        tomcat8.start();
        init();
    }

    private static void init() {
//        initDatabaseConnectionPool();
    }

    /***
     * 初始化连接池
     */
    private static void initDatabaseConnectionPool() {
        java.sql.Connection connection = ConnectionManager.getConnection();
        ConnectionManager.closeAll(connection,null,null);
    }

}
