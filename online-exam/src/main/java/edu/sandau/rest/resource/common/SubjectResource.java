package edu.sandau.rest.resource.common;

import edu.sandau.entity.Subject;
import edu.sandau.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Slf4j
@Path("common/sub")
@Api(value = "课程接口")
public class SubjectResource {
    @Autowired
    private SubjectService subjectService;

    @ApiOperation(value = "新增课程")
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response addSub(Subject subject) throws Exception {
        subjectService.save(subject);
        return Response.ok(subject).build();
    }

    @ApiOperation(value = "更改课程")
    @PUT
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response updateSub(Subject subject) throws Exception {
        subject = subjectService.update(subject);
        return Response.ok(subject).build();
    }

    @ApiOperation(value = "根据组织id查找课程")
    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Subject> getSubByOrgId(@QueryParam("orgId") Integer orgId) throws Exception {
        return subjectService.getSubjectsByOrgId(orgId);
    }

    @ApiOperation(value = "根据id删除课程")
    @DELETE
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response deleteSubById(@QueryParam("id") Integer id) throws Exception {
        subjectService.deleteById(id);
        return Response.ok().build();
    }

}
