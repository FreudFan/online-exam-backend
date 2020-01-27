package edu.sandau.rest.resource;


import edu.sandau.security.Auth;
import edu.sandau.security.SessionWrapper;
import edu.sandau.service.WorryTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("worryTopic")
@Api(value = "错题接口")
@Auth
public class WorryTopicResource {
    @Autowired
    private WorryTopicService worryTopicService;
    @Autowired
    private SessionWrapper sessionWrapper;
    @ApiOperation(value = "查询错题")
    @GET
    @Path("show")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response show(){

        return null;
    }
}
