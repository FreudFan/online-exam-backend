package edu.sandau.dao;

import edu.sandau.entity.LoginUserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class LoginUserSecurityDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public LoginUserSecurity save (LoginUserSecurity userSecurity) throws Exception {
        String SQL = " INSERT INTO login_user_security " +
                "( login_user_id, question, answer ) VALUES " +
                "( ?, ?, ? )";

        List<Object[]> batchArgs = new ArrayList<>(userSecurity.getQuestion().size());
        for (int i = 0; i < userSecurity.getQuestion().size(); i++ ) {
            Object[] obj = new Object[3];
            obj[0] = userSecurity.getLogin_user_id();
            obj[1] = userSecurity.getQuestion().get(i);
            obj[2] = userSecurity.getAnswer().get(i);
            batchArgs.add(obj);
        }

        jdbcTemplate.batchUpdate(SQL, batchArgs);
        return userSecurity;
    }

    public List<String> getQuestionById( Integer id ) throws Exception {
        String SQL = " SELECT question FROM online_exam.login_user_security WHERE login_users_id = ? ";
        List<Map<String,Object>> results = jdbcTemplate.queryForList(SQL, id);
        List questions = results.stream().map(value -> value.get("question") ).collect(Collectors.toList());
        return questions;
    }

}
