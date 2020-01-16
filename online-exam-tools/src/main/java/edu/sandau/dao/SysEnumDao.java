package edu.sandau.dao;

import edu.sandau.entity.SysEnum;
import edu.sandau.rest.model.Page;
import edu.sandau.utils.MapUtil;
import org.apache.commons.collections.MapUtils;
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
public class SysEnumDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SysEnum save(SysEnum sysEnum) {
        String sql = " INSERT INTO sys_enum " +
                "( catalog, type, name, value, description ) VALUES " +
                "( ?, ?, ?, ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sysEnum.getCatalog());
            ps.setString(2, sysEnum.getType());
            ps.setString(3, sysEnum.getName());
            ps.setInt(4, sysEnum.getValue());
            ps.setString(5, sysEnum.getDescription());
            return ps;
        }, keyHolder);
        int keyId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        sysEnum.setId(keyId);
        return sysEnum;
    }

    /***
     * 通过模块和类型获取所有枚举
     * @param catalog
     * @param type
     * @return
     */
    public List<Map<String, Object>> getEnumMap(String catalog, String type) {
        String sql = " SELECT name, value FROM sys_enum WHERE catalog = ? AND type = ? ";
        return jdbcTemplate.queryForList(sql, new Object[]{catalog, type});
    }

    public List<SysEnum> getEnums(String catalog, String type) {
        List<Map<String, Object>> params = this.getEnumMap(catalog, type);
        return (List) MapUtil.mapToObject(params, SysEnum.class);
    }

    /***
     * 通过 模块，类型，整型值 获得枚举
     * @param catalog
     * @param type
     * @param value
     * @return
     */
    public SysEnum getEnum(String catalog, String type, Integer value) {
        String sql = " SELECT * FROM sys_enum WHERE catalog = ? AND type = ? AND value = ? ";
        Map<String, Object> param = jdbcTemplate.queryForMap(sql, new Object[]{catalog, type, value});
        return (SysEnum) MapUtil.mapToObject(param, SysEnum.class);
    }

    public Integer getEnumValue(String catalog, String type, String name) {
        String sql = " SELECT value FROM sys_enum WHERE catalog = ? AND type = ? AND name = ? ";
        Map<String, Object> param = jdbcTemplate.queryForMap(sql, new Object[]{catalog, type, name});
        return MapUtils.getInteger(param, "value");
    }

    public String getEnumName(String catalog, String type, Integer value) {
        String sql = " SELECT name FROM sys_enum WHERE catalog = ? AND type = ? AND value = ? ";
        Map<String, Object> param = jdbcTemplate.queryForMap(sql, new Object[]{catalog, type, value});
        return MapUtils.getString(param, "name");
    }

    public SysEnum getEnumById(Integer id) {
        String sql = " SELECT * FROM sys_enum WHERE id = ? ";
        Map<String, Object> param = jdbcTemplate.queryForMap(sql, new Object[]{id});
        return (SysEnum) MapUtil.mapToObject(param, SysEnum.class);
    }

    public Integer updateEnum(SysEnum sysEnum) {
        String sql = " UPDATE sys_enum " +
                " SET catalog = ?, name = ?, type = ?, value = ?, description = ? " +
                " WHERE id = ? ";
        Object[] param = new Object[5];
        param[0] = sysEnum.getCatalog();
        param[1] = sysEnum.getName();
        param[2] = sysEnum.getType();
        param[3] = sysEnum.getValue();
        param[4] = sysEnum.getDescription();
        return jdbcTemplate.update(sql, param);
    }

    public Integer deleteEnum(SysEnum sysEnum) {
        String sql = " DELETE FROM sys_enum WHERE catalog = ? AND name = ? AND type = ? AND value = ? ";
        Object[] objects = new Object[4];
        objects[0] = sysEnum.getCatalog();
        objects[1] = sysEnum.getName();
        objects[2] = sysEnum.getType();
        objects[3] = sysEnum.getValue();
        return jdbcTemplate.update(sql, objects);
    }

    public Integer deleteEnumById(Integer id) {
        String sql = " DELETE FROM sys_enum WHERE id = ? ";
        return jdbcTemplate.update(sql, new Object[]{id});
    }

    public List<SysEnum> listEnumByPage(Page page) {
        int start = (page.getPageNo() - 1) * page.getPageSize();
        String sql = " SELECT * FROM sys_enum ORDER by id ASC limit ? , ? ";
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, new Object[]{start, page.getPageSize()});
        return (List) MapUtil.mapToObject(mapList, SysEnum.class);
    }

    public Integer getCount() {
        String sql = " SELECT COUNT(1) FROM sys_enum ";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

}
