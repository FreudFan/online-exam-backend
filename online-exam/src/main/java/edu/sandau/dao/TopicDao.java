package edu.sandau.dao;

import edu.sandau.entity.Topic;
import edu.sandau.rest.model.Page;
import edu.sandau.security.SessionWrapper;
import edu.sandau.utils.JDBCUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.*;
import java.util.List;
import java.util.Objects;

@Repository
public class TopicDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SessionWrapper sessionWrapper;

    /***
     * 批量删除题目方法
     * @param idName
     * @param idArrays
     */
    public void deleteTopics(String idName,List<Integer> idArrays){
        JDBCUtil.deleteForRecord("topic","flag",idName,idArrays);
    }

    /***
     * 更新一条记录的flag
     * @param id
     */
    public void deleteTopics(Integer id){
        String sql = "update topic set flag = 0 where id = ?";
        jdbcTemplate.update(sql,id);
    }

    /***
     * 返回主表自增id
     * @param topic
     * @return keyID
     */
    public int save(Topic topic) {
            String sql = " INSERT INTO topic " +
                    "( file_id,type,description,correctkey,topicmark,difficult,analysis,subject_id,user_id) VALUES " +
                    "( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                if(topic.getFile_id() != null) {
                    ps.setInt(1, topic.getFile_id());
                } else {
                    ps.setString(1, null);
                }
                ps.setInt(2, topic.getType());
                ps.setString(3, topic.getDescription());
                ps.setString(4, topic.getCorrectkey());
                ps.setDouble(5, topic.getTopicmark());
                ps.setInt(6, topic.getDifficult());
                ps.setString(7, topic.getAnalysis());
                ps.setInt(8, topic.getSubject_id());
                ps.setInt(9, sessionWrapper.getUserId());
                return ps;
            }, keyHolder);
            int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
            return keyId;
    }

    /***
     * 得到topic表的总题目数量
     * @return 题目总数量
     */
    public int getCount() {
        String sql ="select count(1) from topic";
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }

    /***
     * 分页查询topic表
     * @param page
     * @return List<Topic>
     */
    public List<Topic> listTopicByPage(Page page) {
        int start = (page.getPageNo() - 1) * page.getPageSize();
        String sql = " SELECT * FROM topic where flag = 1 limit ? , ? ";
        List<Topic> list = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Topic.class),new Object[]{start,page.getPageSize()});
        return list;
    }
}
