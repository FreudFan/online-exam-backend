package edu.sandau.dao;

import edu.sandau.entity.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class SubjectDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Subject save(Subject subject) {
        String sql = " INSERT INTO subject " +
                "( name, orgId )" +
                " VALUES " +
                "( ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,subject.getName());
            ps.setInt(2, subject.getOrgId());
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        subject.setId(keyId);
        return subject;
    }

    public Integer update(Subject subject) {
        String sql = " UPDATE subject " +
                " SET name = ?, orgId = ? " +
                " WHERE id = ? ";
        Object[] params = new Object[3];
        params[0] = subject.getName();
        params[1] = subject.getOrgId();
        params[2] = subject.getId();
        return jdbcTemplate.update(sql, params);
    }

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

    public List<Subject> getSubjectsByOrgId(Integer orgId) {
        String sql = " SELECT * FROM subject WHERE orgId = ? ORDER by id ASC ";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<Subject>(Subject.class),orgId);
    }

}
