package edu.sandau.rest;

import com.alibaba.fastjson.JSON;
import edu.sandau.model.LoginUsers;
import edu.sandau.service.EmailService;
import edu.sandau.model.EmailMessage;
import edu.sandau.security.SessionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.*;

@Slf4j
@Path("test")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EmailService emailService;

    @GET
    @Path("mail")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String sendMail() throws Exception {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTos("test@test.com");
        emailMessage.setContent("你好，测试");
        emailMessage.setSubject("test");
        emailService.sendHTMLMail(emailMessage, new HashMap<>());
        return null;
    }

    @GET
    @Path("show")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String topicShow() throws Exception {
        String sql = "SELECT * FROM topics";
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        List list = new ArrayList();
        for ( int i = 0; i < 100000; i++ ) {
            list = jdbcTemplate.queryForList(sql);
        }
        return JSON.toJSONString(list);
//        topticsService.read();
//        return null;
    }

    @POST
    @Path("post")
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })    //使用 application/x-www-form-urlencoded
    @Produces({ MediaType.APPLICATION_JSON })   //使用@FormParam 可接收单个name，如果传的是list可在postman里设多个相同name的参数
    public String post(@FormParam("key") String value, @FormParam("key1") String value1, @FormParam("key2") List<String> value2 ) throws Exception {
        log.info("key = " + value + "\tkey1 = " + value1 + "\tkey2 = " + value2);
        return null;
    }

    @POST
    @Path("postlist")
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @Produces({ MediaType.APPLICATION_JSON })   //使用MultivaluedMap可以接受复杂类型数据，会将所有key的值作为list接收，类似于 Map<List> 类型
    public String postList(MultivaluedMap map) throws Exception {
        log.info(map.toString());
        List l = (List)map.get("key");
        String s = (String) l.get(0);
        log.info(s);
        return null;
    }

    @GET
    @Path("redis")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String redis() throws Exception {
        HashOperations hops = redisTemplate.opsForHash();
        Map<String,String> map=new HashMap<String,String>();
        map.put("key1","value1");
        map.put("key2","value2");
        map.put("key3","value3");
        map.put("key4","value4");
        map.put("key5","value5");
        hops.putAll("spring.key",map);
        return null;
    }

    @Autowired
    private HttpSession httpSession;
    @Autowired
    private SessionWrapper sessionWrapper;
    @GET
    @Path("session")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String session() throws Exception {
        LoginUsers users = new LoginUsers();
        users.setEmail("faf@fdasf.com");
        users.setPassword("11111");
        sessionWrapper.addSessionToRedis(httpSession, users);
        return sessionWrapper.getId(httpSession);
    }

    @GET
    @Path("sessionId")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Object sessionId() throws Exception {
//        return sessionWrapper.getRedisKeyFromSession(httpSession);
//        sessionWrapper.refresh(httpSession);
//        sessionWrapper.invalidate(httpSession);
        LoginUsers users = sessionWrapper.getCurrentUser(httpSession);
        sessionWrapper.setAttribute(httpSession, "23432", "fadsfjaidosfjaijfaidso");
        Map<String,Object> map = sessionWrapper.getAllAttribute(httpSession);
        return map;
    }

}
