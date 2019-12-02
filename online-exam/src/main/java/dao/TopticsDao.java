package dao;

import datasource.ConnectionManager;
import datasource.JDBCUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            for (int i = 1; i < excelList.size(); i++) {
                String description = (String) excelList.get(i).get(0);
                String correctKey = (String) excelList.get(i).get(6);
                Double topicMark = (Double) excelList.get(i).get(7);
                String analysis = (String) excelList.get(i).get(8);
                int id = JDBCUtils.getId(sb.toString(), description, correctKey, topicMark, analysis);
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
                        stmt.setString(3, (String) excelList.get(i).get(j));
                        stmt.executeUpdate();
                    }
                }
            }
        }catch (Throwable e){
            flag = false;
            e.printStackTrace();
        }

       return flag;
    }
}
