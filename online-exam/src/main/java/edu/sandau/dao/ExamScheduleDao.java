package edu.sandau.dao;

import edu.sandau.entity.ExamSchedule;
import edu.sandau.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

    public Integer update(ExamSchedule examSchedule) {
        String sql = " UPDATE exam_schedule " +
                " SET exam_id = ?, beginTime = ?, endTime = ?, type = ?, description = ? " +
                " WHERE id = ? ";
        Object[] params = new Object[6];
        params[0] = examSchedule.getExamId();
        params[1] = examSchedule.getBeginTime();
        params[2] = examSchedule.getEndTime();
        params[3] = examSchedule.getType();
        params[4] = examSchedule.getDescription();
        params[5] = examSchedule.getId();
        return jdbcTemplate.update(sql, params);
    }

    public ExamSchedule getExamScheduleById(Integer id) {
        String sql = " SELECT * FROM exam_schedule WHERE id = ? ";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ExamSchedule.class), new Object[]{id});
    }

    public Integer delete(Integer id) {
        String sql = " DELETE FROM exam_schedule WHERE id = ? ";
        return jdbcTemplate.update(sql, new Object[]{id});
    }
}
