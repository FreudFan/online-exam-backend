package edu.sandau.dao;

import edu.sandau.entity.ExamRecord;
import edu.sandau.entity.Subject;
import edu.sandau.rest.model.exam.ExamRecordAndExamDeatil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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
                "( user_id, exam_id, beginTime )" +
                " VALUES " +
                "( ?, ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, examRecord.getUserId());
            ps.setInt(2, examRecord.getExamId());
            ps.setString(3, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(examRecord.getBeginTime()));
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

    public void updateScoreById(Integer id, Double score) {
        String sql = "UPDATE exam_record SET score = ? where id = ?";
        jdbcTemplate.update(sql, score, id);
    }

    public List<ExamRecordAndExamDeatil> findAll(Integer subjectId, Integer userId) {
        String sql = "SELECT exam_record.id,NAME,totalScore,description,score,beginTime,endTime FROM exam_record " +
                "INNER JOIN exam " +
                "ON exam.id = exam_record.`exam_id` AND subject_id = ? AND user_id = ? ";
        List<ExamRecordAndExamDeatil> exams = jdbcTemplate.query(sql, new BeanPropertyRowMapper<ExamRecordAndExamDeatil>(ExamRecordAndExamDeatil.class), subjectId, userId);
        return exams;
    }

    public List<Subject> getSubjectIdByUserId(Integer userId) {
        String sql = "SELECT DISTINCT subject_id as id FROM exam_record INNER JOIN exam ON exam_id = exam.id WHERE user_id = ? ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Subject>(Subject.class), userId);
    }
}
