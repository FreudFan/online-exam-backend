package edu.sandau;

import edu.sandau.datasource.DruidManager;
import lombok.extern.slf4j.Slf4j;
import server.Tomcat8;

@Slf4j
public class OnlineExamApplication {
    public static void main(String[] args) throws Exception {
        Thread tomcat8 = new Thread(new Tomcat8());
        tomcat8.start();
        init();
    }

    private static void init() throws Exception {
//        initDatabaseConnectionPool();
    }

    /***
     * 初始化连接池
     */
    private static void initDatabaseConnectionPool() {
        java.sql.Connection connection = DruidManager.getConnection();
        DruidManager.closeAll(connection,null,null);
    }

}
