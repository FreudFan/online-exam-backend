package datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DruidUtil {
    private static DruidDataSource dataSource;

    static {
        Properties prop = new Properties();
        try {
            prop.load(DruidUtil.class.getClassLoader().getResourceAsStream("druid.properties"));
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(prop);
            //dataSource.addFilters("stat,log4j,wall");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConn() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
