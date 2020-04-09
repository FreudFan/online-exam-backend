package edu.sandau.dao;

import edu.sandau.entity.Subject;
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
public class SubjectDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Subject save(Subject subject) {
        String sql = " INSERT INTO subject " +
                "( id, name )" +
                " VALUES " +
                "( ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,subject.getId());
            ps.setString(2, subject.getName());
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        subject.setId(keyId);
        return subject;
    }

//    public Integer update(Subject subject) {
//        String sql = " UPDATE subject " +
//                " SET name = ?, orgId = ? " +
//                " WHERE id = ? ";
//        Object[] params = new Object[3];
//        params[0] = subject.getName();
//       params[1] = subject.getOrgId();
//        params[2] = subject.getId();
//        return jdbcTemplate.update(sql, params);
//    }

    public Integer delete(Integer id) {
        String sql = " DELETE FROM subject WHERE id = ? ";
        return jdbcTemplate.update(sql, new Object[]{id});
    }

    public List<Subject> getAll() {
        String sql = " SELECT * FROM subject ORDER by id ASC ";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<Subject>(Subject.class));
    }

    public Subject getSubjectById(Integer id) {
        String sql = " SELECT * FROM subject WHERE id = ? ORDER by id ASC ";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Subject>(Subject.class),new Object[]{id});
    }


    public List<Subject> showSub(Page page) {
        StringBuffer sb = new StringBuffer(" SELECT * FROM subject WHERE 1 = 1 ");
        List<Object> obj = new ArrayList<>();
        Map<String,Object> params = page.getOption();
        String sql = getSqlAndParams(params, obj);
        sb.append(sql);
        sb.append(" LIMIT ? , ? ");
        page.setTotal(getCount(sql,obj));
        Integer pageSize = page.getPageSize();
        Integer pageNo = page.getPageNo();
        obj.add((pageNo - 1 ) * pageSize);
        obj.add(pageSize);
        return jdbcTemplate.query(sb.toString(),new BeanPropertyRowMapper<Subject>(Subject.class),obj.toArray());
    }

    private String getSqlAndParams(Map<String, Object> params, List<Object> obj){
        if(params == null || params.size() <= 0){
            return "";
        }
        StringBuffer sql = new StringBuffer();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            Object value = params.get(key);
            if(value != null && !"".equalsIgnoreCase(value.toString())) {
                if("name".equals(key)) {
                    sql.append(" And " + key + " like ? ");
                    obj.add("%" + value + "%");
                }
            }
        }
        return sql.toString();
    }
    public Integer getCount(String sb, List<Object> obj) {
        String sql = " SELECT count(1) FROM subject WHERE 1= 1 " + sb;
        return jdbcTemplate.queryForObject(sql,Integer.class,obj.toArray());
    }
}
