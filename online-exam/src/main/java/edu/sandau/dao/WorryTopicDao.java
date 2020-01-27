package edu.sandau.dao;


import edu.sandau.entity.WorryTopic;
import edu.sandau.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WorryTopicDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SessionWrapper sessionWrapper;

    public void saveWorryTopic(List<WorryTopic> worryTopicList){
        String sql = "insert into worry_topic" +
                "(user_id,record_id,exam_id,topic_id,worryanswer,correctanswer)" +
                "VALUES(?,?,?,?,?,?)";
        List<Object[]> params = new ArrayList<Object[]>();
        worryTopicList.stream().forEach((worryTopic)->{
            Object[] obj = new Object[]{worryTopic.getUser_id(),worryTopic.getRecord_id()
                    ,worryTopic.getExam_id(),worryTopic.getTopic_id(),worryTopic.getWorryanswer(),worryTopic.getCorrectanswer()};
            params.add(obj);
        });
        jdbcTemplate.batchUpdate(sql,params);
    }
}
