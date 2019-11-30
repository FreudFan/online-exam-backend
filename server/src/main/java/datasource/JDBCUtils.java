package datasource;

import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtils {

    /***
     * 查询返回 ResultSet
     * @param sql
     * @return
     * @throws SQLException
     */
    public static ResultSet queryForResultSet( String sql ) throws SQLException {
        if ( !StringUtils.isEmpty(sql) ) {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery();
        }
        return null;
    }

    /***
     * 查询返回 List<Map<String, Object>>
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> queryForList( String sql ) throws SQLException {
        if ( !StringUtils.isEmpty(sql) ) {
            List<Map<String, Object>> resultList = new ArrayList<>();
            try(
                    Connection connection = ConnectionManager.getConnection();
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery();
                ) {
                ResultSetMetaData metaData = resultSet.getMetaData();   // 取得数据库的列名
                int numberOfColumns = metaData.getColumnCount();


                Map<String, Object> resultMap;
                while ( resultSet.next() ) {
                    resultMap = new HashMap<>(numberOfColumns);
                    for ( int i = 1; i < numberOfColumns; i++ ) {
                        resultMap.put(metaData.getColumnName(i), resultSet.getObject(i));
                    }
                    resultList.add(resultMap);
                }

            } catch ( Exception e ) {
                e.printStackTrace();
                return null;
            }
            return resultList;
        }
        return null;
    }

}
