package edu.sandau.rest.resource;

import edu.sandau.entity.Subject;
import edu.sandau.rest.model.Page;
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

    @ApiOperation(value = "查询所有课程")
    @Path("show")
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response showSub(Page page) throws Exception {
        if(page == null){
            List<Subject> subjects = subjectService.getAll();
            return Response.ok(subjects).build();
        }else {
            page = subjectService.showSub(page);
        }
        return Response.ok(page).build();
    }



    @ApiOperation(value = "新增课程")
    @Path("insert")
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response addSub(Subject subject) throws Exception {
        subjectService.save(subject);
        return Response.ok(subject).build();
    }

//    @ApiOperation(value = "更改课程")
//    @PUT
//    @Consumes({ MediaType.APPLICATION_JSON })
//    @Produces({ MediaType.APPLICATION_JSON })
//    public Response updateSub(Subject subject) throws Exception {
//        subject = subjectService.update(subject);
//        return Response.ok(subject).build();
//    }



    @ApiOperation(value = "根据id删除课程")
    @Path("delete")
    @DELETE
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response deleteSubById(@QueryParam("id") Integer id) throws Exception {
        subjectService.deleteById(id);
        return Response.ok().build();
    }

}
