package service;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import entity.UserInfo;

@Path("users")

public interface UserService {

	@GET
    @Path("all")  //访问http://localhost:8888/demo/rest/demo/users/all
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, UserInfo> queryAll();

	@POST
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UserInfo addUser(UserInfo user, @QueryParam("requestid") int requestid);
	
	@GET
	@Path("/loginInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public UserInfo login(@QueryParam("userAccount") String account, @QueryParam("userPassword") String password);
	

}
