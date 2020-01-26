package edu.sandau.dao;

import edu.sandau.entity.Exam;
import edu.sandau.rest.model.Page;
import edu.sandau.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ExamDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SessionWrapper sessionWrapper;

    public Exam save(Exam exam) {
        String sql = " INSERT INTO exam " +
                "(subject_id ,name, totalScore, description, flag, createBy, updateBy )" +
                " VALUES " +
                "( ?,?, ?, ?, ?, ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, exam.getSubject_id());
            ps.setString(2, exam.getName());
            ps.setInt(3, exam.getTotalScore());
            ps.setString(4, exam.getDescription());
            ps.setInt(5, exam.getFlag());
            ps.setInt(6, sessionWrapper.getUserId());
            ps.setInt(7, sessionWrapper.getUserId());
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        exam.setId(keyId);
        return exam;
    }

    public void saveDetail(Exam exam){
        String sql = "INSERT INTO exam_detail(exam_id,topic_id) VALUES(?,?)";
        List<Integer> topicsId = exam.getTopicsId();
        int keyId = exam.getId();
        List<Object[]> params = new ArrayList<Object[]>();
        topicsId.stream().forEach((id)->{
            Object[] obj = new Object[]{keyId,id};
            params.add(obj);
        });
        jdbcTemplate.batchUpdate(sql,params);
    }

    public List<Exam> listExamByPage(Page page, int flag){
        return null;
    }

    public int getCount(int flag) {
        int count = 0;
        String sql = "select count(1) from exma where flag = ?";
         count = jdbcTemplate.queryForObject(sql, Integer.class, flag);
        return count;
    }
}
