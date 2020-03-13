package edu.sandau.rest.resource.auth;

import edu.sandau.validate.code.ValidateCodeParam;
import edu.sandau.validate.code.ValidateCodeProcessorHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("auth/validate")
@Api(value = "验证码接口")
public class ValidateResource {

    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /***
     * 发邮件、短信验证
     *  to 短信、邮件
     *  type 邮件：email， 短信：sms
     * @return
     */
    @POST
    @Path("code/{type}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getVerificationCode(@PathParam("type") String type,
                                        ValidateCodeParam validateCodeParam,
                                        @Context HttpServletRequest request,
                                        @Context HttpServletResponse response) throws Exception {
        validateCodeProcessorHolder.findValidateCodeProcessor(type).create(request, validateCodeParam);
        return Response.ok().build();
    }

    @ApiOperation(value = "验证码校验", response = Boolean.class)
    @POST
    @Path("code/check/{type}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Boolean checkVerificationCode(@PathParam("type") String type,
                                          ValidateCodeParam validateCodeParam,
                                          @Context HttpServletRequest request,
                                          @Context HttpServletResponse response) throws Exception {
        return validateCodeProcessorHolder.findValidateCodeProcessor(type).validate(request, validateCodeParam);
    }
}
