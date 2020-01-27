package edu.sandau.dao;

import edu.sandau.entity.ExamSchedule;
import edu.sandau.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
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
        String sql = " INSERT INTO exam_schedule " +
                "( exam_id, beginTime, endTime, type, setterId, description )" +
                " VALUES " +
                "( ?, ?, ?, ?, ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            Date beginTime = null;
            Date endTime = null;
            if (examSchedule.getBeginTime() != null) {
                beginTime = new Date(examSchedule.getBeginTime().getTime());
            }
            if(examSchedule.getEndTime() != null) {
                endTime = new Date(examSchedule.getEndTime().getTime());
            }
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, examSchedule.getExamId());
            ps.setDate(2, beginTime);
            ps.setDate(3, endTime);
            ps.setInt(4, examSchedule.getType());
            ps.setInt(5, sessionWrapper.getUserId());
            ps.setString(6, examSchedule.getDescription());
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        examSchedule.setId(keyId);
        return examSchedule;
    }

    public Integer delete(Integer id) {
        String sql = " DELETE FROM exam_schedule WHERE id = ? ";
        return jdbcTemplate.update(sql, new Object[]{id});
    }
}
