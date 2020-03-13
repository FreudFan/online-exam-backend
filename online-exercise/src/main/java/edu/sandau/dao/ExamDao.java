package edu.sandau.dao;

import edu.sandau.entity.Exam;
import edu.sandau.entity.ExamDetail;
import edu.sandau.entity.Topic;
import edu.sandau.rest.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
            ps.setInt(6, 1);
            ps.setInt(7, 1);
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        exam.setId(keyId);
        return exam;
    }

    public void saveDetail(Exam exam){
        String sql = "INSERT INTO exam_detail(exam_id,topic_id,topicmark) VALUES(?,?,?)";
        List<Topic> topics = exam.getTopics();
        int keyId = exam.getId();
        List<Object[]> params = new ArrayList<Object[]>();
        topics.stream().forEach((topic)->{
            Object[] obj = new Object[]{keyId,topic.getId(),topic.getTopicmark()};
            params.add(obj);
        });
        jdbcTemplate.batchUpdate(sql,params);
    }


    public List<Exam> listExamByPage(Page page, int flag){
        int start = (page.getPageNo() - 1) * page.getPageSize();
        String sql = " SELECT * FROM exam where flag = ? limit ? , ? ";
        List<Exam> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Exam.class), new Object[]{flag, start, page.getPageSize()});
        return list;
    }

    public int getCount(int flag) {
        int count = 0;
        String sql = "select count(1) from exam where flag = ?";
        count = jdbcTemplate.queryForObject(sql, Integer.class, flag);
        return count;
    }


    public List<ExamDetail> listExamDetail(Integer id) {
        String sql = "select topic_id,topicmark from exam_detail where exam_id = ?";
        List<ExamDetail> idList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ExamDetail.class), id);
        return idList;
    }

    public void deleteExam(Integer id) {
        String sql = "update exam set flag = 0 where id = ?";
        jdbcTemplate.update(sql,id);
    }
}
