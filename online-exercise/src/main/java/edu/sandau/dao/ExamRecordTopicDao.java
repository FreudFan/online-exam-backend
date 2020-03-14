package edu.sandau.dao;

import edu.sandau.entity.ExamRecordTopic;
import edu.sandau.rest.model.exam.ExamDetailAndWorryTopic;
import edu.sandau.rest.model.exam.ExamTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ExamRecordTopicDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ExamRecordTopic save(ExamRecordTopic examRecordTopic) {
        String sql = " INSERT INTO exam_record_topic " +
                "( record_id, topic_id, answer )" +
                " VALUES " +
                "( ?, ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, examRecordTopic.getRecordId());
            ps.setInt(2, examRecordTopic.getTopicId());
            ps.setString(3, examRecordTopic.getAnswer());
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        examRecordTopic.setId(keyId);
        return examRecordTopic;
    }

    public void saveBatch(ExamTopic examTopic) throws Exception {
        String sql = " INSERT INTO exam_record_topic " +
                "( record_id, topic_id, answer )" +
                " VALUES " +
                "( ?, ?, ? )";
        List<Object[]> params = new ArrayList<>();
        Integer recordId = examTopic.getRecordId();
        List<ExamTopic> examTopics = examTopic.getTopics();
        for (ExamTopic topic : examTopics) {
            params.add(new Object[]{recordId, topic.getTopicId(), topic.getAnswer()});
        }
        int[] num = jdbcTemplate.batchUpdate(sql, params);
        if (num.length != examTopics.size()) {
            throw new SQLException("刷新题目失败");
        }
    }

    /***
     * 根据主键更新
     * @param examRecordTopic
     * @return
     */
    public Integer updateById(ExamRecordTopic examRecordTopic) {
        String sql = " UPDATE exam_record_topic " +
                " SET record_id = ?, topic_id = ?, answer = ? " +
                " WHERE id = ? ";
        Object[] params = new Object[4];
        params[0] = examRecordTopic.getRecordId();
        params[1] = examRecordTopic.getTopicId();
        params[2] = examRecordTopic.getAnswer();
        params[3] = examRecordTopic.getId();
        return jdbcTemplate.update(sql, params);
    }

    public Integer updateByRecordIdAndTopicId(String answer, Integer recordId, Integer topicId) {
        String sql = " UPDATE exam_record_topic " +
                " SET answer = ? " +
                " WHERE record_id = ? AND topic_id = ? ";
        return jdbcTemplate.update(sql, new Object[]{answer, recordId, topicId});
    }

    public Integer delete(Integer id) {
        String sql = " DELETE FROM exam_record_topic WHERE id = ? ";
        return jdbcTemplate.update(sql, new Object[]{id});
    }

    public Integer deleteByRecordId(Integer recordId) {
        String sql = " DELETE FROM exam_record_topic WHERE record_id = ? ";
        return jdbcTemplate.update(sql, new Object[]{recordId});
    }

    public ExamRecordTopic getRecordTopicByElements(Integer recordId, Integer topicId) {
        String sql = " SELECT * FROM exam_record_topic WHERE record_id = ? AND topic_id = ? ";
        List<ExamRecordTopic> recordTopics = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ExamRecordTopic.class), new Object[]{recordId, topicId});
        if (recordTopics.size() > 0) {
            return recordTopics.get(0);
        } else {
            return null;
        }
    }

    public ExamRecordTopic getTopicById(Integer id) {
        String sql = " SELECT * FROM exam_record_topic WHERE id = ? ";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ExamRecordTopic.class), new Object[]{id});
    }

    public List<ExamRecordTopic> getTopicsByRecordId(Integer id) {
        String sql = " SELECT * FROM exam_record_topic WHERE record_id = ? ORDER by id ASC ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ExamRecordTopic.class), id);
    }

    public List<ExamRecordTopic> getRecordAnswersByRecordId(Integer id) {
        String sql = " SELECT topic_id topicId, answer FROM exam_record_topic WHERE record_id = ? ORDER by id ASC ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ExamRecordTopic.class), id);
    }

    public List<ExamDetailAndWorryTopic> findDetailByRecordId(Integer recordId) {
        String sql = "SELECT a.record_id,a.topic_id,description,correctkey,answer,analysis FROM exam_record_topic AS a " +
                "INNER JOIN topic AS b ON a.topic_id = b.id AND a.record_id = ? ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<ExamDetailAndWorryTopic>(ExamDetailAndWorryTopic.class), recordId);
    }
}
