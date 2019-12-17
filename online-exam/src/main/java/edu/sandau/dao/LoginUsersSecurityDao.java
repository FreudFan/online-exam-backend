package edu.sandau.dao;

import edu.sandau.model.LoginUsersSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class LoginUsersSecurityDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public LoginUsersSecurity save (LoginUsersSecurity usersSecurity ) throws Exception {
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

    public List<String> getQuestionById( Integer id ) throws Exception {
        String SQL = " SELECT question FROM online_exam.login_users_security WHERE login_users_id = ? ";
        List<Map<String,Object>> results = jdbcTemplate.queryForList(SQL, id);
        List questions = results.stream().map(value -> value.get("question") ).collect(Collectors.toList());
        return questions;
    }

}
