package edu.sandau.dao;

import edu.sandau.entity.LoginUser;
import edu.sandau.rest.model.Page;
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
import java.util.Objects;

@Repository
public class LoginUserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /***
     * 插入返回自增主键
     * @param loginUser
     * @return
     */
    public LoginUser save(LoginUser loginUser) {
        String sql = " INSERT INTO login_user " +
                "( username, password, realname, gender, email, telephone, role, " +
                " school_id, college_id, major_id, class_id ) VALUES " +
                "( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

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
            ps.setInt(8, loginUser.getSchool_id());
            ps.setInt(9, loginUser.getCollege_id());
            ps.setInt(10, loginUser.getMajor_id());
            ps.setString(11, loginUser.getClass_id());
            return ps;
        }, keyHolder);

        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        loginUser.setId(keyId);
        return loginUser;
    }

    public List<LoginUser> getUserByRealname(String realname ) {
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
     */
    public List<LoginUser> getUserByFields(List<String> keys, List<String> values) {
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
            return (List) MapUtil.mapToObject(mapList, LoginUser.class);
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

    public LoginUser getUserById(Integer id) throws Exception {
        String sql = " SELECT * FROM login_user WHERE id = ? ";
        Map<String,Object> map = jdbcTemplate.queryForMap(sql, new Object[]{id});
        return (LoginUser) MapUtil.mapToObject(map, LoginUser.class);
    }

    /***
     * 更新用户
     * @param user
     * @return
     * @throws Exception
     */
    public Integer update(LoginUser user) throws Exception {
        String sql = " UPDATE login_user " +
                " SET username = ?, realname = ?, gender = ?, email = ?, telephone = ?, " +
                " school_id = ? ,college_id = ?, major_id = ?, class_id = ?, " +
                " role = ? " +
                " WHERE id = ? ";
        Object[] param = new Object[11];
        param[0] = user.getUsername();
        param[1] = user.getRealname();
        param[2] = user.getGender();
        param[3] = user.getEmail();
        param[4] = user.getTelephone();
        param[5] = user.getSchool_id();
        param[6] = user.getCollege_id();
        param[7] = user.getMajor_id();
        param[8] = user.getClass_id();
        param[9] = user.getRole();
        param[10] = user.getId();
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
        String sql = "UPDATE login_user SET " + column + " = ? WHERE id = ? ";
        int num = jdbcTemplate.update(sql, new Object[]{value, id});
        return num > 0;
    }

    /***
     * 分页查询所有用户
     * @param page
     * @return
     * @throws Exception
     */
    public List<LoginUser> listUserByPage(Page page) throws Exception {
        int start = (page.getPageNo() - 1) * page.getPageSize();
        String sql = " SELECT * FROM login_user ORDER BY id ASC limit ? , ? ";
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, new Object[]{start, page.getPageSize()});
        return (List) MapUtil.mapToObject(mapList, LoginUser.class);
    }

    /***
     * 查询总数
     * @return
     * @throws Exception
     */
    public Integer getCount() throws Exception {
        String sql = " SELECT COUNT(1) FROM login_user ";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

}
