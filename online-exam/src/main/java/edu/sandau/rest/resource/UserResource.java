package edu.sandau.rest.resource;

import edu.sandau.rest.model.Page;
import edu.sandau.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("user")
@Api(value = "用户接口")
//@Auth
public class UserResource {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "分页查询用户", response = List.class)
    @GET
    @Path("list")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response list(Page page) throws Exception {
        page = userService.getUsersByPage(page);
        return Response.ok(page).build();
    }

    @ApiOperation(value = "重置密码", response = Boolean.class,
            notes = "检查 username，email，telephone 是否唯一")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "Integer", required = true ),
            @ApiImplicitParam(name = "password", value = "新密码", dataType = "String", required = true )
    })
    @POST
    @Path("reset-password")
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response resetPassword(@FormParam("id") Integer id, @FormParam("password") String password) throws Exception {
        boolean ok = userService.resetPassword(id, password);
        if ( ok ) {
            return Response.ok(true).build();
        }
        return Response.accepted(false).status(500).build();
    }

}
