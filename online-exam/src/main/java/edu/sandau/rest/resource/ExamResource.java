package edu.sandau.rest.resource;


import edu.sandau.entity.Exam;
import edu.sandau.rest.model.Page;
import edu.sandau.security.Auth;
import edu.sandau.security.SessionWrapper;
import edu.sandau.service.ExamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.*;

@Path("exam")
@Api(value = "试卷接口")
@Auth
public class ExamResource {


    @Autowired
    private ExamService examService;

    /***
     * 传参格式:
     *{
     * 	"name":"试卷一",
     * 	"totalScore":100,
     * 	"flag":1,
     * 	"subject_id":2,
     * 	"description":"test试卷",
     * 	"topicsId":[2225,2226,2227,2228,2230,2234]
     * }
     * @param exam
     * @return
     */
    @ApiOperation(value = "保存用户自定义的试卷")
    @POST
    @Path("save")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response saveExam(Exam exam){
        examService.saveExam(exam);
        return Response.accepted("ok").build();
    }

    @ApiOperation(value = "查询所有试卷")
    @GET
    @Path("show")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response showExam(Page page){
        if (page == null) {
            page = new Page();
        }
        page = examService.getExamByPage(page,1);
        return Response.ok(page).build();
    }
}
