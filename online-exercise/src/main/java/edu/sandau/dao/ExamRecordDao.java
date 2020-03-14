package edu.sandau.dao;


import edu.sandau.entity.ExamRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
                "( user_id, exam_id, beginTime )" +
                " VALUES " +
                "( ?, ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, examRecord.getUserId());
            ps.setInt(2, examRecord.getExamId());
            ps.setTimestamp(3, new java.sql.Timestamp(examRecord.getBeginTime().getTime()));
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        examRecord.setId(keyId);
        return examRecord;
    }


    public ExamRecord getRecordById(Integer id) {
        String sql = " SELECT * FROM exam_record WHERE id = ? ORDER by id ASC ";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ExamRecord.class), new Object[]{id});
    }


    public Integer updateEndTimeById(ExamRecord examRecord) {
        String sql = " UPDATE exam_record SET endTime = ? WHERE id = ? ";
        return jdbcTemplate.update(sql, new Object[]{examRecord.getEndTime(), examRecord.getId()});
    }
}
