package edu.sandau.dao;

import edu.sandau.entity.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

@Repository
public class TestDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Test save(Test test) {
        String sql = " INSERT INTO test " +
                "( name, totalScore, description, flag, createBy, updateBy )" +
                " VALUES " +
                "( ?, ?, ?, ?, ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,test.getName());
            ps.setInt(2, test.getTotalScore());
            ps.setString(3, test.getDescription());
            ps.setInt(4, test.getFlag());
            ps.setInt(5, test.getCreateBy());
            ps.setInt(6, test.getUpdateBy());
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        test.setId(keyId);
        return test;
    }

}
