package edu.sandau.rest.resource;

import edu.sandau.entity.SysEnum;
import edu.sandau.rest.model.Page;
import edu.sandau.service.SysEnumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Api(value = "枚举接口")
@Path("enum")
public class EnumResource {
    @Autowired
    private SysEnumService enumService;

    @ApiOperation(value = "根据模块和类型查询枚举集合")
    @GET
    @Path(value = "/{catalog}/{type}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Map<String,Object>> getEnum(@PathParam("catalog") String catalog, @PathParam("type") String type) {
        catalog = catalog.toUpperCase();
        type = type.toUpperCase();
        return enumService.getEnumMap(catalog, type);
    }

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
    @Path("defaultPass")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String getDefaultPassword() {
        return enumService.getEnumName("COMMON", "DEFAULT_PASSWORD", 1);
    }

    @ApiOperation(value = "查询所有枚举")
    @GET
    @Path("all")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public List<SysEnum> listAll() {
        return enumService.getAllEnums();
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
