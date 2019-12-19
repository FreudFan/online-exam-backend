package edu.sandau.dao;

import edu.sandau.model.EmailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class EmailVoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(EmailMessage emailMessage ) throws Exception {
        String SQL = " INSERT INTO email_message " +
                "( tos, subject, content ) VALUES " +
                "( ?, ?, ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, emailMessage.getTos());
            ps.setString(2, emailMessage.getSubject());
            ps.setString(3, emailMessage.getContent());
            return ps;
        }, keyHolder);

        int keyId = keyHolder.getKey().intValue();
        emailMessage.setEmail_message_id(keyId);
    }

}
