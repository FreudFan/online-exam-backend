package edu.sandau.dao;

import edu.sandau.utils.MapUtil;
import model.LoginUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class LoginUsersDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /***
     * 插入返回自增主键
     * @param loginUsers
     * @return
     */
    public LoginUsers save( LoginUsers loginUsers ) throws Exception {
        String SQL = " INSERT INTO login_users " +
                "( username, password, realname, gender, email, telephone, subject_id, role ) VALUES " +
                "( ?, ?, ?, ?, ?, ?, ?, ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, loginUsers.getUsername());
            ps.setString(2, loginUsers.getPassword());
            ps.setString(3, loginUsers.getRealname());
            ps.setInt(4, loginUsers.getGender());
            ps.setString(5,loginUsers.getEmail());
            ps.setString(6,loginUsers.getTelephone());
            ps.setString(7,loginUsers.getSubject_id());
            ps.setInt(8, loginUsers.getRole());
            return ps;
        }, keyHolder);

        int keyId = keyHolder.getKey().intValue();
        loginUsers.setLogin_users_id(keyId);
        return loginUsers;
    }

    public List<LoginUsers> getUserByRealname( String realname ) throws Exception {
        String SQL = " SELECT * FROM login_users WHERE realname = ? ";
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(SQL, realname);
        if (mapList.size() == 0) {
            return null;
        } else {
            return (List) MapUtil.mapToObject(mapList, LoginUsers.class);
        }
    }

    /***
     * 通过 username, telephone, email 查询
     * @param keys 数据库列名
     * @param values 列对应的值
     * @return
     * @throws Exception
     */
    public LoginUsers getUserByFields(List<String> keys, List<String> values) throws Exception {
        StringBuilder SQL = new StringBuilder(" SELECT * FROM login_users WHERE 1=1 ");
        if ( keys.size() == values.size() && keys.size() > 0 ) {
            SQL.append(" AND ");
            for ( int i = 0; i < keys.size(); i++ ) {
                if ( i != 0 ) {
                    SQL.append(" OR ");
                }
                SQL.append(keys.get(i)).append(" = ? ");
            }
        }
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList(SQL.toString(), values.toArray());
        if (mapList.size() == 0) {
            return null;
        } else {
            return (LoginUsers) MapUtil.mapToObject(mapList.get(0), LoginUsers.class);
        }
    }

    public LoginUsers login(String loginValue, String loginNmae, String password) throws  Exception {
        String SQL = " SELECT * FROM login_users WHERE " + loginValue + " = ? AND password = ? ";
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList(SQL, new Object[]{loginNmae, password});
        if (mapList.size() == 0) {
            return null;
        } else {
            return (LoginUsers) MapUtil.mapToObject(mapList.get(0), LoginUsers.class);
        }
    }

    public boolean updateUserById(Integer id, String column, String value ) throws Exception {
        String SQL = "UPDATE login_users SET " + column + " = ? WHERE login_users_id = ? ";
        int num = jdbcTemplate.update(SQL, new Object[]{value, id});
        return num > 0;
    }

}
