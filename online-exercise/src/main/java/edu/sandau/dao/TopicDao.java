package edu.sandau.dao;

import edu.sandau.entity.Topic;
import edu.sandau.rest.model.Page;
import edu.sandau.security.RequestContent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class TopicDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /***
     * 批量禁用题目flag
     * @param ids
     */
    public void deleteTopics(Integer... ids) {
        String sql = "update topic set flag = 0 where id = ?";
        List<Object[]> params = new ArrayList<Object[]>();
        for (Integer id : ids) {
            Object[] o = new Object[]{id};
            params.add(o);
        }
        jdbcTemplate.batchUpdate(sql,params);
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
            if (topic.getFile_id() != null) {
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
            ps.setInt(9, RequestContent.getCurrentUser().getId());
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return keyId;
    }

    /***
     * 得到topic表的总题目数量
     * @return 题目总数量
     */
    public int getCount(int flag) {
        String sql = "select count(1) from topic where flag = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, flag);
    }

    /***
     * 分页查询topic表
     * @param page
     * @return List<Topic>
     */
    public List<Topic> listTopicByPage(Page page, int flag) {
        int start = (page.getPageNo() - 1) * page.getPageSize();
        String sql = " SELECT * FROM topic where flag = ? limit ? , ? ";
        List<Topic> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Topic.class), new Object[]{flag, start, page.getPageSize()});
        return list;
    }

    /***
     * 查询试卷的试题内容
     * @param ids
     * @param role
     * @return
     */
    public List<Topic> listTopicByIds(List<Integer> ids, Integer role){
        //0为学生，1为管理员，管理员返回全部内容
        StringBuilder sb;
        if(role == 0) {
           sb = new StringBuilder("SELECT id,description,topicmark FROM topic where 1 = 1 ");
        }
        else {
            sb = new StringBuilder("SELECT * FROM topic where 1 = 1 ");
        }
        List<String> idsString = ids.stream().map(x -> x + "").collect(Collectors.toList());
        String id = StringUtils.join(idsString, ',');
        sb.append(" and id in (" + id + ")");
        List<Topic> topics = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<>(Topic.class));
        return topics;
    }

    /***
     * 根据题目类型、难度、科目查询可用题目
     * @param subjectId
     * @param type
     * @param difficult
     * @return
     */
    public List<Topic> getTopicByTypeAndDifficult(Integer subjectId, Integer type, Integer difficult) {
        String sql = " SELECT * FROM topic WHERE flag = 1 AND type = ? AND difficult = ? AND subject_id = ? ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Topic.class), new Object[]{type, difficult, subjectId});
    }

}
