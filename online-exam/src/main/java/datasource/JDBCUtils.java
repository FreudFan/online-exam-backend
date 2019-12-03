package datasource;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
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
            try(
                    Connection connection = ConnectionManager.getConnection();
                    PreparedStatement statement = connection.prepareStatement(sql);
            ) {
                return statement.executeQuery();
            } catch ( Exception e ) {
                e.printStackTrace();
                return null;
            }
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

                while ( resultSet.next() ) {
                    Map<String, Object> resultMap = new HashMap<>(numberOfColumns);
                    for ( int i = 1; i <= numberOfColumns; i++ ) {
                        resultMap.put(metaData.getColumnName(i), resultSet.getObject(i));
                    }
                    resultList.add(resultMap);
                }
                return resultList;
            } catch ( Exception e ) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static int getId (String sql,String description,String correctKey,double topicMark,String analysis)throws SQLException {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, description);
        ps.setString(2, correctKey);
        ps.setDouble(3, topicMark);
        ps.setString(4, analysis);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int id = rs.getInt(1);
        return id;
    }

    /***
     * 插入实体类实现 insertOrUpdate (未实现)
     * key_id 为 null ? INSERT : UPDATE
     * @param object
     * @return  key_id
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Integer insertOrUpdate( Object object ) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = object.getClass();
        String clazzName = clazz.getName(); //类名
        clazzName = clazzName.substring(clazzName.lastIndexOf(".")+1);
        //表名
        String TABLE_NAME = clazzName.toLowerCase();
        boolean INSERT_FLAG = true;
        Field[] fields = clazz.getDeclaredFields(); //获取所有属性对象
        for (Field field1 : fields) {
            String field = field1.getName();

            if (field.endsWith(TABLE_NAME + "_id")) {   //判断主键的值是否存在，如果不存在则insert
                field1.setAccessible(true);
                if (field1.get(object) == null) {
                    INSERT_FLAG = false; //key为空 使用insert
                }
            }
        }

        return null;
    }

}
