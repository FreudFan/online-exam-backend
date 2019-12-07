package rest;

import lombok.extern.slf4j.Slf4j;
import model.LoginUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Path("/")
public class LoginController {

    @Autowired
    private UserService userService;

    @POST
    @Path("login")
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @Produces({ MediaType.APPLICATION_JSON })
    public ResponseEntity login(MultivaluedMap multivaluedMap) throws Exception {
        Map<String,Object> map = new HashMap<>(3);
        map.put("errno",0);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @POST
    @Path("register")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public LoginUsers register(LoginUsers loginUsers) throws Exception {
//        loginUsers = userService.addUser(loginUsers);
        Map<String,Object> map = new HashMap<>(3);
        map.put("errno",0);
        return loginUsers;
    }

}
