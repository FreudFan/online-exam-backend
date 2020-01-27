package edu.sandau.dao;

import edu.sandau.entity.ExamRecordTopic;
import edu.sandau.utils.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;
import java.util.Objects;

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
        params[2] = examRecordTopic.getId();
        return jdbcTemplate.update(sql, params);
    }

    public Integer updateByRecordIdAndTopicId(String answer, Integer recordId, Integer topicId) {
        String sql = " UPDATE exam_record_topic " +
                " SET answer = ? " +
                " WHERE record_id = ?, topic_id = ? ";
        return jdbcTemplate.update(sql, new Object[]{answer, recordId, topicId});
    }

    public Integer delete(Integer id) {
        String sql = " DELETE FROM exam_record_topic WHERE id = ? ";
        return jdbcTemplate.update(sql, new Object[]{id});
    }

    public ExamRecordTopic getExamRecordTopicById(Integer id) {
        String sql = " SELECT * FROM exam_record_topic WHERE id = ? ORDER by id ASC ";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{id});
        return (ExamRecordTopic) MapUtil.mapToObject(map, ExamRecordTopic.class);
    }

}
