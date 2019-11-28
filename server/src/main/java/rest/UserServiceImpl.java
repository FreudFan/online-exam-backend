package rest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import entity.UserInfo;
import util.FileDB;

@Path("users")

public class UserServiceImpl implements service.UserService {
	
	public static Map<String, UserInfo> uMap = FileDB.loadUsers();
	static {
		
	}
	
	
	@GET
    @Path("all")  //访问http://localhost:8888/demo/departs
    @Produces(MediaType.APPLICATION_JSON)
	@Override
	public Map<String, UserInfo> queryAll() {
		// TODO Auto-generated method stub
		return uMap;
	}

	
	@POST
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	@Override
	public UserInfo addUser(UserInfo user,@QueryParam("requestid") int requestid) {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        String createTime = format.format(new Date());      //默认为系统当前时间，json序列化date会有问题
		user.setCreateTime(createTime);
		uMap.put(user.getUserId(), user);
		FileDB.saveUsers(uMap);
		return user;
	}
	
	@GET
	@Path("/loginInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public UserInfo login(@QueryParam("userAccount")String account,@QueryParam("userPassword") String password) {
		UserInfo user;
		if(uMap.containsKey(account)) {
			user = uMap.get(account);
			if(user.getUserPassword().equals(password))
				return user;
		}
		return null;
	}

	

}
