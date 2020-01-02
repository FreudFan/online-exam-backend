package edu.sandau.datasource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Scope("singleton")
public class DruidManager extends ContextLoaderListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(DruidManager.class);

    private static DataSource dataSource;

    /***
     * 1.初始化Druid连接池
     */
    private static void init() {
        //第二种方式:使用软编码通过配置文件初始化DBCP
        try {
            Properties properties = new Properties();
            //通过类加载器加载配置文件
            InputStream inputStream = DruidManager.class.getClassLoader().getResourceAsStream("druid.properties");
            properties.load(inputStream);
            LOGGER.info("配置文件：" + properties.toString());
            if ( dataSource == null ) {
                dataSource = DruidDataSourceFactory.createDataSource(properties);
                LOGGER.info("配置连接池成功...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 2.获取连接
     * @return
     */
    public synchronized static Connection getConnection() {
        try {
            if ( dataSource == null ) {
                init();
            }
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized static DataSource getDataSource() {
        if ( dataSource == null ) {
            init();
        }
        return dataSource;
    }

    /***
     * 3.关闭连接
     * @param connection
     * @param statement
     * @param resultSet
     */
    public synchronized static void closeAll(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent event){
        LOGGER.info("启动连接池...");
        init();
        closeAll(getConnection(), null, null);
    }

}
