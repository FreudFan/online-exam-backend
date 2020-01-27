package edu.sandau.dao;

import edu.sandau.entity.ExamRecordTopic;
import edu.sandau.entity.ExamSchedule;
import edu.sandau.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

@Repository
public class ExamScheduleDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SessionWrapper sessionWrapper;

    public ExamSchedule save(ExamSchedule examSchedule) {
        String sql = " INSERT INTO exam_record_topic " +
                "( exam_id, beginTime, type, setterId, description )" +
                " VALUES " +
                "( ?, ?, ?, ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, examSchedule.getExamId());
            ps.setDate(2, new java.sql.Date(examSchedule.getBeginTime().getTime()));
            ps.setInt(3, examSchedule.getType());
            ps.setInt(4, sessionWrapper.getUserId());
            ps.setString(5, examSchedule.getDescription());
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        examSchedule.setId(keyId);
        return examSchedule;
    }

    public Integer delete(Integer id) {
        String sql = " DELETE FROM exam_record_topic WHERE id = ? ";
        return jdbcTemplate.update(sql, new Object[]{id});
    }
}
