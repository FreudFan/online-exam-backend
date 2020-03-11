package edu.sandau.dao;

import edu.sandau.entity.User;
import edu.sandau.rest.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /***
     * 插入返回自增主键
     * @param loginUser
     * @return
     */
    public User save(User loginUser) {
        String sql = " INSERT INTO user " +
                "( username, password, realname, gender, email, telephone, role ) VALUES " +
                "( ?, ?, ?, ?, ?, ?, ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, loginUser.getUsername());
            ps.setString(2, loginUser.getPassword());
            ps.setString(3, loginUser.getRealname());
            ps.setInt(4, loginUser.getGender());
            ps.setString(5, loginUser.getEmail());
            ps.setString(6, loginUser.getTelephone());
            ps.setInt(7, loginUser.getRole());
            return ps;
        }, keyHolder);

        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        loginUser.setId(keyId);
        return loginUser;
    }

    public List<User> getUserByRealname(String realname ) {
        String sql = " SELECT * FROM user WHERE realname = ? ";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), realname);
        if (users.size() == 0) {
            return null;
        } else {
            return users;
        }
    }

    /***
     * 通过 username, telephone, email 查询
     * @param keys 数据库列名
     * @param values 列对应的值
     * @return
     */
    public List<User> getUserByFields(List<String> keys, List<String> values) {
        StringBuilder sql = new StringBuilder(" SELECT * FROM user WHERE 1=1 ");
        if ( keys.size() == values.size() && keys.size() > 0 ) {
            sql.append(" AND ");
            for ( int i = 0; i < keys.size(); i++ ) {
                if ( i != 0 ) {
                    sql.append(" OR ");
                }
                sql.append(keys.get(i)).append(" = ? ");
            }
        }
        List<User> loginUsers = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(User.class), values.toArray());
        if (loginUsers.size() == 0) {
            return null;
        } else {
            return loginUsers;
        }
    }

    public User login(String loginValue, String loginNmae, String password) throws  Exception {
        String sql = " SELECT * FROM user WHERE " + loginValue + " = ? AND password = ? ";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), new Object[]{loginNmae, password});
        if (users.size() == 0) {
            return null;
        } else {
            return users.get(0);
        }
    }

    public User getUserById(Integer id) throws Exception {
        String sql = " SELECT * FROM user WHERE id = ? ";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), new Object[]{id});
    }

    /***
     * 更新用户
     * @param user
     * @return
     * @throws Exception
     */
    public Integer update(User user) throws Exception {
        String sql = " UPDATE user " +
                " SET username = ?, realname = ?, gender = ?, email = ?, telephone = ?, " +
                " role = ? " +
                " WHERE id = ? ";
        Object[] param = new Object[7];
        param[0] = user.getUsername();
        param[1] = user.getRealname();
        param[2] = user.getGender();
        param[3] = user.getEmail();
        param[4] = user.getTelephone();
        param[5] = user.getRole();
        param[6] = user.getId();
        return jdbcTemplate.update(sql, param);
    }

    /***
     * 更新用户指定属性
     * @param id
     * @param column
     * @param value
     * @return
     * @throws Exception
     */
    public Boolean updateUserById(Integer id, String column, String value ) throws Exception {
        String sql = "UPDATE user SET " + column + " = ? WHERE id = ? ";
        int num = jdbcTemplate.update(sql, new Object[]{value, id});
        return num > 0;
    }

    /***
     * 分页查询所有用户
     * @param page
     * @return
     * @throws Exception
     */
    public List<User> listUserByPage(Page page) throws Exception {
        int start = (page.getPageNo() - 1) * page.getPageSize();
        String sql = " SELECT * FROM user ORDER BY id ASC limit ? , ? ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), new Object[]{start, page.getPageSize()});
    }

    /***
     * 查询总数
     * @return
     * @throws Exception
     */
    public Integer getCount() throws Exception {
        String sql = " SELECT COUNT(1) FROM user ";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

}
