package edu.sandau.dao;

import edu.sandau.entity.ExamRecord;
import edu.sandau.utils.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
            ps.setDate(3, new java.sql.Date(examRecord.getBeginTime().getTime()));
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        examRecord.setId(keyId);
        return examRecord;
    }

    public Integer updateById(ExamRecord examRecord) {
        String sql = " UPDATE exam_record SET ";
        List<Object> params = new ArrayList<>();
        List<String> sqlList = new ArrayList<>();
        if (examRecord.getScheduleId() != null) {
            sqlList.add(" record_id = ? ");
            params.add(examRecord.getScheduleId());
        }
        if (examRecord.getScore() != null) {
            sqlList.add(" score = ? ");
            params.add(examRecord.getScore());
        }
        if (examRecord.getEndTime() != null) {
            sqlList.add(" endTime = ? ");
            params.add(examRecord.getEndTime());
        }
        if (params.isEmpty()) {
            return null;
        }
        sql += " SET " + StringUtils.join(sqlList);
        sql += " WHERE id = ? ";
        return jdbcTemplate.update(sql, params);
    }

    public Integer delete(Integer id) {
        String sql = " DELETE FROM exam_record WHERE id = ? ";
        return jdbcTemplate.update(sql, new Object[]{id});
    }

    public ExamRecord getExamRecordById(Integer id) {
        String sql = " SELECT * FROM exam_record WHERE id = ? ORDER by id ASC ";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{id});
        return (ExamRecord) MapUtil.mapToObject(map, ExamRecord.class);
    }

}
