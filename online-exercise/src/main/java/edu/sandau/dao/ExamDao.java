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
import java.util.*;

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


    public List<Exam> listExamByPage(Page page){
        StringBuffer sb = new StringBuffer(" SELECT * FROM exam where 1 = 1 ");
        List<Object> obj = new ArrayList<>();
        Map<String,Object> params = page.getOption();
        String sql = getSqlAndParams(params, obj);
        sb.append(sql);
        sb.append(" limit ? , ? ");
        page.setTotal(getCount(sql,obj));
        int start = (page.getPageNo() - 1) * page.getPageSize();
        obj.add(start);
        obj.add(page.getPageSize());
        List<Exam> list = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<>(Exam.class), obj.toArray());
        return list;
    }

    public int getCount(String sb, List<Object> obj ) {
        int count = 0;
        String sql = "select count(1) from exam where 1 = 1 " + sb;
        count = jdbcTemplate.queryForObject(sql, Integer.class, obj.toArray());
        return count;
    }

    // 获取动态sql
    private String getSqlAndParams(Map<String, Object> params, List<Object> obj){
        if(params == null || params.size() <= 0){
            return "";
        }
        StringBuffer sql = new StringBuffer();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            Object value = params.get(key);
            sql.append(" And " + key + "= ?");
            obj.add(value);
        }
        return sql.toString();
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
