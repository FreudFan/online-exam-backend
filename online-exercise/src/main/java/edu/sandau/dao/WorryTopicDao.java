package edu.sandau.dao;

import edu.sandau.entity.WorryTopic;
import edu.sandau.rest.model.Page;
import edu.sandau.rest.model.WorryTopicData;
import edu.sandau.security.RequestContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class WorryTopicDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveWorryTopicBatch(List<WorryTopic> worryTopicList) {
        String sql = "insert into worry_topic" +
                "(user_id,record_id,exam_id,topic_id,worryanswer,correctanswer)" +
                "VALUES(?,?,?,?,?,?)";
        List<Object[]> params = new ArrayList<>();
        worryTopicList.forEach((worryTopic) -> {
            Object[] obj = new Object[]{worryTopic.getUser_id(), worryTopic.getRecord_id()
                    , worryTopic.getExam_id(), worryTopic.getTopic_id(), worryTopic.getWorryanswer(), worryTopic.getCorrectanswer()};
            params.add(obj);
        });
        jdbcTemplate.batchUpdate(sql, params);
    }

    public void saveWorryTopic(WorryTopic worryTopic) {
        String sql = "insert into worry_topic" +
                "(user_id,record_id,exam_id,topic_id,worryanswer,correctanswer,worrycount)" +
                "VALUES(?,?,?,?,?,?,?)";

        jdbcTemplate.update(sql, worryTopic.getUser_id(), worryTopic.getRecord_id(), worryTopic.getExam_id(),
                worryTopic.getTopic_id(), worryTopic.getWorryanswer(), worryTopic.getCorrectanswer(), 1);
    }

    public void deleteById(Integer recordId, Integer userId) {
        String sql = "delete from worry_topic where record_id = ? and user_id = ?";
        jdbcTemplate.update(sql, recordId, userId);
    }

    public List<WorryTopic> findById(Integer userId, Integer topicId) {
        String sql = "select * from worry_topic where user_id = ? and topic_id = ?";
        List<WorryTopic> worryTopicList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WorryTopic.class), userId, topicId);
        return worryTopicList;
    }

    public List<WorryTopicData> selectAll(Page page) {
        Map<String, Object> params = page.getOption();
        if (params == null || params.size() == 0) {
            params = new HashMap<>(10);
        }
        List<Object> obj = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT t.id,t.`description`,t.`correctkey`,wt.`worryanswer`,wt.`worrycount`,t.`difficult`,t.`analysis`,t.`subject_id`,wt.`createtime` " +
                "FROM worry_topic AS wt INNER JOIN topic t ON wt.topic_id = t.`id` where  1 = 1 ");

        String sqlAppend = getSqlAndParams(params, obj);
        sql.append(sqlAppend);
        int count = getCount(sql.toString(), obj);
        page.setTotal(count);
        if (page.getPageSize() != null && page.getPageNo() != null) {
            int start = (page.getPageNo() - 1) * page.getPageSize();
            sql.append(" limit ?,?");
            obj.add(start);
            obj.add(page.getPageSize());
        }
        return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(WorryTopicData.class), obj.toArray());
    }

    public int getCount(String sql, List<Object> obj) {
        String sqlCount = "select count(1) from  ( " + sql + " ) a";
        return jdbcTemplate.queryForObject(sqlCount, Integer.class, obj.toArray());
    }

    /***
     * 拼接动态Sql
     * @param params
     * @param obj
     * @return
     */
    private String getSqlAndParams(Map<String, Object> params, List<Object> obj) {
        StringBuilder sql = new StringBuilder();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            Object value = params.get(key);
            if ("user_id".equalsIgnoreCase(key)) {
                sql.append(" AND wt.").append(key).append(" = ?");
            } else {
                sql.append(" AND  ").append(key).append(" = ?");
            }
            obj.add(value);
        }
        //如果前端没有传user_id，则将查询当前登录用户的错题记录
        if (!params.containsKey("user_id")) {
            sql.append(" And wt.user_id = ?");
            obj.add(RequestContent.getCurrentUser().getId());
        }
        return sql.toString();
    }

    public void updateWorryCount(WorryTopic wt) {
        String sql = "update worry_topic set worrycount = ? where user_id = ? and topic_id = ?";
        jdbcTemplate.update(sql, wt.getWorrycount() + 1, wt.getUser_id(), wt.getTopic_id());
    }

    public WorryTopic findWorryTopicByRecordId(Integer record_id, Integer topic_id) {
        String sql = "select worryanswer,worrycount from worry_topic where record_id = ? and topic_id = ? ";
        List<WorryTopic> worryTopics = jdbcTemplate.query(sql, new BeanPropertyRowMapper<WorryTopic>(WorryTopic.class), record_id, topic_id);
        if (worryTopics.size() == 0) {
            return null;
        }
        return worryTopics.get(0);
    }
}
