package edu.sandau.rest.resource;

import edu.sandau.entity.LoginUser;
import edu.sandau.rest.model.TestParam;
import edu.sandau.rest.model.User;
import edu.sandau.service.EmailService;
import edu.sandau.entity.EmailMessage;
import edu.sandau.security.SessionWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Component
@Api(value = "测试接口")
@Slf4j
@Path("test")
//@Auth
public class TestResource {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EmailService emailService;
/*
    @GET //1
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_XML)
    public List<Department> list() {
        List<Department> dept = new ArrayList<>();
        dept.add(new Department(1L, "dept1"));
        dept.add(new Department(2L, "dept2"));
        return dept;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_XML)
    public Department get(@PathParam("id") Long id) {
        return new Department(id, "dept2");
    }

    @POST //2
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_XML)
    public Department save(@FormParam("name") String name) {
        Department d = new Department(1L, name);
        return d;
    }

    @PUT //3
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_XML)
    public Department update(@PathParam("id") Long id, @FormParam("name") String name) {
        Department d = new Department(id, name);
        return d;
    }

    @DELETE //4
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public void delete(@PathParam("id") Long id) {
        System.out.println("删除部门：" + id);
    }
*/

    @ApiOperation(value = "根据姓名查询基本信息", response = String.class)
    @GET
    @Path("mail")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String sendMail() throws Exception {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setEmail("test@test.com");
        emailMessage.setContent("你好，测试");
        emailMessage.setSubject("test");
//        emailService.sendHTMLMail(emailMessage, new HashMap<>());
        return null;
    }

    @PUT
    @Path("show")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response topicShow(TestParam queryParams) throws Exception {
        queryParams.getPass();
        return Response.ok().build();
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
        Map<String,String> map=new HashMap<>(5);
        map.put("key1","value1");
        map.put("key2","value2");
        map.put("key3","value3");
        map.put("key4","value4");
        map.put("key5","value5");
        hops.putAll("spring.key",map);
        return null;
    }

    @Autowired
    private SessionWrapper sessionWrapper;
    @Context
    private SecurityContext securityContext;

    @GET
    @Path("session")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String session() throws Exception {
        LoginUser user = new LoginUser();
        user.setEmail("faf@fdasf.com");
        user.setPassword("11111");
//        sessionWrapper.addSessionToRedis(securityContext, user);
        return null;
    }

    @GET
    @Path("sessionId")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Object sessionId() throws Exception {
//        return sessionWrapper.getRedisKeyFromSession(httpSession);
//        sessionWrapper.refresh(httpSession);
//        sessionWrapper.invalidate(httpSession);
        User users = sessionWrapper.getCurrentUser(securityContext);
//        sessionWrapper.setAttribute(httpSession, "23432", "fadsfjaidosfjaijfaidso");
//        Map<String,Object> map = sessionWrapper.getAllAttribute(httpSession);
        return users;
    }

    @GET
    @Path("res")
    public Response get() {
        return Response.ok("fafadsdf").status(HttpStatus.BAD_REQUEST.value()).build();
    }

}
