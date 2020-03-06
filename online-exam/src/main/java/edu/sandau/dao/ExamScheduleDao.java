package edu.sandau.dao;

import edu.sandau.entity.ExamSchedule;
import edu.sandau.security.RequestContent;
import edu.sandau.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository
public class ExamScheduleDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ExamSchedule save(ExamSchedule examSchedule) {
        String sql = " INSERT INTO exam_schedule " +
                "( exam_id, beginTime, endTime, type, setterId, description )" +
                " VALUES " +
                "( ?, ?, ?, ?, ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            Timestamp beginTime = null;
            Timestamp endTime = null;
            if (examSchedule.getBeginTime() != null) {
                beginTime = new Timestamp(examSchedule.getBeginTime().getTime());
            }
            if(examSchedule.getEndTime() != null) {
                endTime = new Timestamp(examSchedule.getEndTime().getTime());
            }
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, examSchedule.getExamId());
            ps.setTimestamp(2, beginTime);
            ps.setTimestamp(3, endTime);
            ps.setInt(4, examSchedule.getType());
            ps.setInt(5, RequestContent.getCurrentUser().getId());
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
        List<ExamSchedule> schedules = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ExamSchedule.class), new Object[]{id});
        if (schedules.isEmpty()) {
            return null;
        } else {
            return schedules.get(0);
        }
    }

    /***
     * 根据id查询可用日程
     * @param id
     * @return
     */
    public ExamSchedule getAccessExamScheduleById(Integer id) {
        String sql = " SELECT * FROM exam_schedule WHERE flag = 1 AND id = ? ";
        List<ExamSchedule> schedules = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ExamSchedule.class), new Object[]{id});
        if (schedules.isEmpty()) {
            return null;
        } else {
            return schedules.get(0);
        }
    }

    public Integer delete(Integer id) {
        String sql = " DELETE FROM exam_schedule WHERE id = ? ";
        return jdbcTemplate.update(sql, new Object[]{id});
    }

    public Integer clock(Integer id) {
        String sql = " UPDATE exam_schedule SET flag = 0 WHERE id = ? ";
        return jdbcTemplate.update(sql, new Object[]{id});
    }

    public Integer getExamIdById(Integer id){
        String sql = "select exam_id from exam_schedule where id = ?";
        return jdbcTemplate.queryForObject(sql,Integer.class,id);
    }
}
