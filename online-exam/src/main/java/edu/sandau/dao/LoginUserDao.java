package edu.sandau.dao;

import edu.sandau.entity.LoginUser;
import edu.sandau.utils.MapUtil;
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
public class LoginUserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /***
     * 插入返回自增主键
     * @param loginUser
     * @return
     */
    public LoginUser save(LoginUser loginUser) throws Exception {
        String sql = " INSERT INTO login_user " +
                "( username, password, realname, gender, email, telephone, subject_id, role ) VALUES " +
                "( ?, ?, ?, ?, ?, ?, ?, ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, loginUser.getUsername());
            ps.setString(2, loginUser.getPassword());
            ps.setString(3, loginUser.getRealname());
            ps.setInt(4, loginUser.getGender());
            ps.setString(5, loginUser.getEmail());
            ps.setString(6, loginUser.getTelephone());
            ps.setString(7, loginUser.getSubject_id());
            ps.setInt(8, loginUser.getRole());
            return ps;
        }, keyHolder);

        int keyId = keyHolder.getKey().intValue();
        loginUser.setLogin_user_id(keyId);
        return loginUser;
    }

    public List<LoginUser> getUserByRealname(String realname ) throws Exception {
        String sql = " SELECT * FROM login_user WHERE realname = ? ";
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, realname);
        if (mapList.size() == 0) {
            return null;
        } else {
            return (List) MapUtil.mapToObject(mapList, LoginUser.class);
        }
    }

    /***
     * 通过 username, telephone, email 查询
     * @param keys 数据库列名
     * @param values 列对应的值
     * @return
     * @throws Exception
     */
    public LoginUser getUserByFields(List<String> keys, List<String> values) throws Exception {
        StringBuilder sql = new StringBuilder(" SELECT * FROM login_user WHERE 1=1 ");
        if ( keys.size() == values.size() && keys.size() > 0 ) {
            sql.append(" AND ");
            for ( int i = 0; i < keys.size(); i++ ) {
                if ( i != 0 ) {
                    sql.append(" OR ");
                }
                sql.append(keys.get(i)).append(" = ? ");
            }
        }
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList(sql.toString(), values.toArray());
        if (mapList.size() == 0) {
            return null;
        } else {
            return (LoginUser) MapUtil.mapToObject(mapList.get(0), LoginUser.class);
        }
    }

    public LoginUser login(String loginValue, String loginNmae, String password) throws  Exception {
        String sql = " SELECT * FROM login_user WHERE " + loginValue + " = ? AND password = ? ";
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList(sql, new Object[]{loginNmae, password});
        if (mapList.size() == 0) {
            return null;
        } else {
            return (LoginUser) MapUtil.mapToObject(mapList.get(0), LoginUser.class);
        }
    }

    public boolean updateUserById(Integer id, String column, String value ) throws Exception {
        String sql = "UPDATE login_user SET " + column + " = ? WHERE login_user_id = ? ";
        int num = jdbcTemplate.update(sql, new Object[]{value, id});
        return num > 0;
    }

}
