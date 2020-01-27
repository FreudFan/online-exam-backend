package edu.sandau.rest.resource;

import edu.sandau.entity.ExamSchedule;
import edu.sandau.security.Auth;
import edu.sandau.service.ExamScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("exam/schedule")
@Auth
@Api(value = "考试日程接口")
public class ExamScheduleResource {

    @Autowired
    private ExamScheduleService examScheduleService;

    @ApiOperation(value = "新增日程, 其中endTime是可选项")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(ExamSchedule examSchedule){
        examSchedule.setId(null);
        examSchedule = examScheduleService.saveOrUpdate(examSchedule);
        return Response.ok(examSchedule).build();
    }

    @ApiOperation(value = "更新日程")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(ExamSchedule examSchedule){
        examSchedule = examScheduleService.saveOrUpdate(examSchedule);
        return Response.ok(examSchedule).build();
    }

}
