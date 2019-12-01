package dao;

import datasource.JDBCUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TopticsDao {

    public List selectTopicAll() throws Exception {
        List<Map<String,Object>> list = new ArrayList<>();
        String sql = "select * from topics";
        list = JDBCUtils.queryForList(sql);
        return list;
    }
}
