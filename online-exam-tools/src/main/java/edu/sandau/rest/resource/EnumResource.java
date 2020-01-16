package edu.sandau.rest.resource;

import edu.sandau.rest.model.Page;
import edu.sandau.service.SysEnumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Api(value = "枚举接口")
@Path("enum")
public class EnumResource {
    @Autowired
    private SysEnumService enumService;

    @ApiOperation(value = "性别枚举")
    @GET
    @Path("gender")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Map<String,Object>> getGenderEnum() {
        return enumService.getEnumMap("COMMON", "GENDER");
    }

    @ApiOperation(value = "默认密码")
    @GET
    @Path("default")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String getDefaultPassword() {
        return enumService.getEnumName("COMMON", "DEFAULT_PASSWORD", 1);
    }

    @ApiOperation(value = "分页查询所有枚举")
    @GET
    @Path("list")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Page list(Page page) {
        if (page == null) {
            return null;
        }
        return enumService.getEnumsByPage(page);
    }
}
