package dao;

import datasource.ConnectionManager;
import datasource.JDBCUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TopticsDao {

    public List selectTopicAll() throws Exception {
        List<Map<String,Object>> list = new ArrayList<>();
        String sql = "select * from topics";
        list = JDBCUtils.queryForList(sql);
        return list;
    }



    /***
     * 插入execl
     * @param excelList
     * @return flag
     */
    public static boolean insetForExcel( List<List<Object>> excelList)  {
        boolean flag = true;
        StringBuffer sb = new StringBuffer();
        StringBuffer sbForOptions = new StringBuffer();
        sb.append("INSERT INTO topics(description,correctkey,topicmark,analysis)VALUES(?,?,?,?);" );
        sbForOptions.append("INSERT INTO options(topics_id,options.option,options.value)VALUES(?,?,?);" );
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sbForOptions.toString());
            ResultSet rs ;
            PreparedStatement ps = connection.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            for (int i = 1; i < excelList.size(); i++) {
                String description = String.valueOf(excelList.get(i).get(0));
                String correctKey = String.valueOf(excelList.get(i).get(6));
                Double topicMark = (Double) excelList.get(i).get(7);
                String analysis = String.valueOf(excelList.get(i).get(8));
                ps.setString(1, description);
                ps.setString(2, correctKey);
                ps.setDouble(3, topicMark);
                ps.setString(4, analysis);
                ps.executeUpdate();
                rs =  ps.getGeneratedKeys();
                rs.next();
                int id = rs.getInt(1);
                for (int j = 1; j < 6; j++) {
                    String option = "";
                    switch (j) {
                        case 1:
                            option = "A";
                            break;
                        case 2:
                            option = "B";
                            break;
                        case 3:
                            option = "C";
                            break;
                        case 4:
                            option = "D";
                            break;
                        case 5:
                            option = "E";
                            break;
                    }
                    if (excelList.get(i).get(j) != "") {
                        stmt.setInt(1, id);
                        stmt.setString(2, option);
                        stmt.setString(3, String.valueOf(excelList.get(i).get(j)));
                        stmt.addBatch();
                    }
                }
                stmt.executeBatch();
            }
        }catch (Throwable e){
            flag = false;
            e.printStackTrace();
        }

       return flag;
    }
}
