package edu.sandau.rest.resource;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("test")
@Api(value = "测试")
public class TestResource {

    @Autowired
    HttpSession httpSession;

    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String topic() {
        httpSession.setAttribute("123", "123");
        return httpSession.getAttribute("123").toString();
    }

}
