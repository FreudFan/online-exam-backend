package datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;

import javax.annotation.sql.DataSourceDefinition;
import javax.servlet.ServletContextEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Scope("singleton")
public class ConnectionManager extends ContextLoaderListener {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static DruidDataSource dataSource;

    //1.初始化Druid连接池
    private void init() {
        //第二种方式:使用软编码通过配置文件初始化DBCP
        try {
            Properties properties = new Properties();
            //通过类加载器加载配置文件
            InputStream inputStream = ConnectionManager.class.getClassLoader().getResourceAsStream("druid.properties");
            properties.load(inputStream);
            System.out.println("配置文件：：" + properties.toString());
            if ( dataSource == null ) {
                dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
                LOGGER.info("配置连接池成功...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //2.获取连接
    public synchronized static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //3.关闭连接
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
        this.init();
        closeAll(getConnection(), null, null);
    }

}
