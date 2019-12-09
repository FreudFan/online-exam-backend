package dao;

import model.LoginUsersSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LoginUsersSecurityDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public LoginUsersSecurity save ( LoginUsersSecurity usersSecurity ) throws Exception {
        String SQL = " INSERT INTO login_users_security " +
                "( login_users_id, question, answer ) VALUES " +
                "( ?, ?, ? )";

        List<Object[]> batchArgs = new ArrayList<>(usersSecurity.getQuestion().size());
        for ( int i = 0; i < usersSecurity.getQuestion().size(); i++ ) {
            Object[] obj = new Object[3];
            obj[0] = usersSecurity.getLogin_users_id();
            obj[1] = usersSecurity.getQuestion().get(i);
            obj[2] = usersSecurity.getAnswer().get(i);
            batchArgs.add(obj);
        }

        jdbcTemplate.batchUpdate(SQL, batchArgs);
        return usersSecurity;
    }

}
