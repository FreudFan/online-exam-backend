package edu.sandau.dao;

import edu.sandau.entity.ExamRecord;
import edu.sandau.rest.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class ExamRecordDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /***
     * 第一次插入时 不插入 score, endTime
     * @param examRecord
     * @return
     */
    public ExamRecord save(ExamRecord examRecord) {
        String sql = " INSERT INTO exam_record " +
                "( user_id, schedule_id, beginTime )" +
                " VALUES " +
                "( ?, ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, examRecord.getUserId());
            ps.setInt(2, examRecord.getScheduleId());
            ps.setTimestamp(3, new java.sql.Timestamp(examRecord.getBeginTime().getTime()));
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        examRecord.setId(keyId);
        return examRecord;
    }

    public Integer updateEndTimeById(ExamRecord examRecord) {
        String sql = " UPDATE exam_record SET endTime = ? WHERE id = ? ";
        return jdbcTemplate.update(sql, new Object[]{examRecord.getEndTime(), examRecord.getId()});
    }

    public Integer delete(Integer id) {
        String sql = " DELETE FROM exam_record WHERE id = ? ";
        return jdbcTemplate.update(sql, new Object[]{id});
    }

    public ExamRecord getRecordById(Integer id) {
        String sql = " SELECT * FROM exam_record WHERE id = ? ORDER by id ASC ";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ExamRecord.class), new Object[]{id});
    }

    public List<ExamRecord> getRecordsByUserId(Integer userId, Page page) {
        int start = (page.getPageNo() - 1) * page.getPageSize();
        String sql = " SELECT * FROM exam_record WHERE user_id = ? limit ? , ? ";
        Object[] params = new Object[]{userId, start, page.getPageSize()};
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ExamRecord.class), params);
    }

    public Integer getRecordsCountByUserId(Integer userId) {
        String sql = "SELECT count(1) FROM topic WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    public ExamRecord getRecordByUserIdAndScheduleId(Integer userId, Integer scheduleId) {
        String sql = " SELECT * FROM exam_record WHERE user_id = ? AND schedule_id = ? ";
        Object[] params = new Object[]{userId, scheduleId};
        List<ExamRecord> records = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ExamRecord.class), params);
        if(records.isEmpty()) {
            return null;
        } else {
            return records.get(0);
        }
    }

    public List<ExamRecord> getRecordsByScheduleId(Integer scheduleId) {
        String sql = " SELECT * FROM exam_record WHERE schedule_id = ? ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ExamRecord.class), scheduleId);
    }

    public void updateScoreById(Integer id,Double score) {
        String sql = "UPDATE exam_record SET score = ? where id = ?";
        jdbcTemplate.update(sql,score,id);
    }
}
