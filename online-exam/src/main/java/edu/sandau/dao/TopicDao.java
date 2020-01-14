package edu.sandau.dao;

import edu.sandau.datasource.DruidManager;
import edu.sandau.rest.model.TopicData;
import edu.sandau.service.TopicService;
import edu.sandau.utils.JDBCUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TopicDao {

    @Autowired
    private TopicService topicService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List selectTopicAll() throws Exception {
        List<Map<String,Object>> list = new ArrayList<>();
        String sql = "select a.id, description,correctkey,topicmark,analysis,b.option,b.value from topic  a " +
                "left join options  b on a.id = b.topic_id where flag = 1";
        list = JDBCUtil.queryForList(sql);
        return list;
    }

    /***
     * 插入execl
     * @param excelList
     * @return flag
     */
    public boolean insetForExcel( List<List<Object>> excelList)  {
        boolean flag = true;
        StringBuffer sb = new StringBuffer();
        StringBuffer sbForOptions = new StringBuffer();
        sb.append("INSERT INTO topic(description,correctkey,topicmark,analysis)VALUES(?,?,?,?);" );
        sbForOptions.append("INSERT INTO options(topic_id,options.option,options.value)VALUES(?,?,?);" );
        try {
            Connection connection = DruidManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sbForOptions.toString());
            ResultSet rs ;
            PreparedStatement ps = connection.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            int chooseCount = topicService.getChooseCount(excelList.get(0));
            for (int i = 1; i < excelList.size(); i++) {
                String description = String.valueOf(excelList.get(i).get(0));
                String correctKey = String.valueOf(excelList.get(i).get(chooseCount + 1));
                String mark = excelList.get(i).get(chooseCount + 2).toString();
                Double topicMark = null;
                if(!StringUtils.isEmpty(mark)){
                    topicMark = Double.valueOf(excelList.get(i).get(chooseCount + 2).toString());
                }
                String analysis = String.valueOf(excelList.get(i).get(chooseCount + 3));
                ps.setString(1, description);
                ps.setString(2, correctKey);
                if(topicMark == null) {
                    ps.setString(3,null);
                }else{
                    ps.setDouble(3, topicMark);
                }
                ps.setString(4, analysis);
                ps.executeUpdate();
                rs =  ps.getGeneratedKeys();
                rs.next();
                int id = rs.getInt(1);
                for (int j = 1; j <= chooseCount ; j++) {
                    if (excelList.get(i).get(j) != "") {
                        stmt.setInt(1, id);
                        stmt.setString(2,  String.valueOf((char)('A' + j - 1)));
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


    public int deleteTopics(String idName,String[] idArrays){
        int count = JDBCUtil.deleteForRecord("topic","flag",idName,idArrays);
        return count;
    }

    public int saveTopics(TopicData data) {
        String sql = " INSERT INTO topic " +
                "( file_id,type,difficult,description,correctkey,topicmark,analysis,subject_id) VALUES " +
                "( ?, ?, ?, ?, ?, ?, ?, ? )";
        int id = data.getId();
        int subject_id = data.getSubject_id();
        int type = data.getType();
        List<List<String>> topicList = data.getFile();
        List<Object[]> batchArgs = new ArrayList<>();
//        jdbcTemplate.update(sql,)
        return 0;
    }
}
