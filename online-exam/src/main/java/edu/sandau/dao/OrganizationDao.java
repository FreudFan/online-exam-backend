package edu.sandau.dao;

import edu.sandau.entity.LoginUser;
import edu.sandau.enums.OrganizationEnum;
import edu.sandau.entity.Organization;
import edu.sandau.utils.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class OrganizationDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Organization save(Organization organization) {
        String sql = " INSERT INTO organization " +
                "( name, type, upper_id )" +
                " VALUES " +
                "( ?, ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, organization.getName());
            ps.setInt(2, organization.getType());
            ps.setInt(3, organization.getUpper_id());;
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        organization.setId(keyId);
        return organization;
    }

    public Integer update(Organization organization) throws Exception {
        String sql = " UPDATE organization " +
                " SET name = ?, type = ?, upper_id = ? " +
                " WHERE id = ? ";
        Object[] params = new Object[4];
        params[0] = organization.getName();
        params[1] = organization.getType();
        params[2] = organization.getUpper_id();
        params[3] = organization.getId();
        return jdbcTemplate.update(sql, params);
    }

    public Organization getOrganizationById(Integer id) {
        String sql = " SELECT * FROM organization WHERE id = ? ";
        Map<String,Object> map = jdbcTemplate.queryForMap(sql, new Object[]{id});
        return (Organization) MapUtil.mapToObject(map, Organization.class);
    }

    public List<Organization> listAll() {
        String sql = " SELECT * FROM organization ORDER by id ASC ";
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        return (List) MapUtil.mapToObject(mapList, Organization.class);
    }

    /***
     * 通过 类型 和 上级id 获取指定类型列表
     * @param type
     * @param upperId
     * @return
     */
    public List<Organization> getOrgByValueAndUpperId(Integer type, Integer upperId) {
        String sql = " SELECT * FROM organization WHERE type = ? ";
        List params = new ArrayList(2);
        params.add(type);
        if ( upperId == null && !type.equals(OrganizationEnum.SCHOOL.getValue())) {
            sql += " AND upper_id = ? ";
            params.add(upperId);
        }
        sql += " ORDER BY id ASC ";
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, params);
        return (List) MapUtil.mapToObject(mapList, Organization.class);
    }

    /***
     * 根据上级id查询下级项
     * @param upperId
     * @return
     */
    public List<Organization> getOrgByUpperId(Integer upperId) {
        String sql = " SELECT * FROM organization WHERE upper_id = ? ";
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, new Object[]{upperId});
        return (List) MapUtil.mapToObject(mapList, Organization.class);
    }

}
