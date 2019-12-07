package dao;

import model.LoginUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer addUser( LoginUsers loginUsers ) {
//        loginUsers.getTABLE_NAME();
        return null;
    }

    public List<LoginUsers> getUserByRealname( String realname ) throws EmptyResultDataAccessException {
        String SQL = " SELECT * FROM login_users WHERE realname = ? ";
        List<LoginUsers> users = jdbcTemplate.queryForList(SQL, LoginUsers.class, new Object[]{realname});
        return users;
    }
}
