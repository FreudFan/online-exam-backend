package rest;

import com.alibaba.fastjson.JSON;
import datasource.ConnectionManager;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("test")
public class Test {

    @GET
    @Path("show")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String topicShow() throws Exception {
        String sql = "SELECT * FROM topics";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        List list = new ArrayList();
        for ( int i = 0; i < 100000; i++ ) {
            list = jdbcTemplate.queryForList(sql);
        }
        return JSON.toJSONString(list);
//        topticsService.read();
//        return null;
    }

}
