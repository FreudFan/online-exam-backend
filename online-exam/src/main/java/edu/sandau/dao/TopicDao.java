package edu.sandau.dao;

import edu.sandau.datasource.DruidManager;
import edu.sandau.entity.LoginUser;
import edu.sandau.entity.Topic;
import edu.sandau.enums.DifficultTypeEnum;
import edu.sandau.rest.model.TopicData;
import edu.sandau.service.TopicService;
import edu.sandau.utils.JDBCUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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


    public int deleteTopics(String idName,String[] idArrays){
        int count = JDBCUtil.deleteForRecord("topic","flag",idName,idArrays);
        return count;
    }

    public int save(Topic topic) {
            String sql = " INSERT INTO topic " +
                    "( file_id,type,description,correctkey,topicmark,difficult,analysis,subject_id) VALUES " +
                    "( ?, ?, ?, ?, ?, ?, ?, ? )";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1,topic.getFile_id());
                ps.setInt(2, topic.getType());
                ps.setString(3, topic.getDescription());
                ps.setString(4, topic.getCorrectkey());
                ps.setDouble(5, topic.getTopicmark());
                ps.setInt(6, topic.getDifficult());
                ps.setString(7, topic.getAnalysis());
                ps.setInt(8, topic.getSubject_id());
                return ps;
            }, keyHolder);

            int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();

            return keyId;
    }
}
