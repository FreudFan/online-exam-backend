package edu.sandau.rest.resource.auth;

import edu.sandau.security.SessionUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("auth/open")
@Api(value = "获取sessionId")
public class OpenResource {
    @Autowired
    private SessionUtils sessionUtils;

    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String getToken() throws Exception {
        return sessionUtils.createToken();
    }
}
