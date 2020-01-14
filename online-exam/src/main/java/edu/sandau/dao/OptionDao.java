package edu.sandau.dao;
import edu.sandau.entity.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class OptionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public void insertOption(int keyId, List<Option> optionList){
      String  sql = " INSERT INTO options " +
                "( topic_id,name,content) VALUES " +
                   "( ?, ?, ? )";
      List<Object[]> params = new ArrayList<>();
        for (int i = 0; i < optionList.size(); i++) {
            Option optionObject = optionList.get(i);
            String option = optionObject.getName();
            String value = optionObject.getContent();
            Object[] obj = new Object[]{keyId,option,value};
            params.add(obj);
        }
        jdbcTemplate.batchUpdate(sql,params);
    }

    public List<Option> findOptionById(int id){
        String sql = "select * from options where topic_id = ?";
        List<Option> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Option.class),id);
        return list;
    }
}
