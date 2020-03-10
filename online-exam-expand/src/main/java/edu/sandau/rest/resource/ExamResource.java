package edu.sandau.rest.resource;

import edu.sandau.entity.Exam;
import edu.sandau.entity.Topic;
import edu.sandau.rest.model.exam.ExamModel;
import edu.sandau.rest.model.Page;
import edu.sandau.security.Auth;
import edu.sandau.service.ExamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.*;
import java.util.List;

@Slf4j
@Path("exam/paper")
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
     * 	"topics":[
     *                {
     * 		"id":2040,
     * 		"topicmark":20
     *        }
     * 	]
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

    @ApiOperation(value = "查询所有可用试卷")
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

    @ApiOperation(value = "查询所有禁用试卷")
    @GET
    @Path("showDelete")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response showDeleteExam(Page page){
        if (page == null) {
            page = new Page();
        }
        page = examService.getExamByPage(page,0);
        return Response.ok(page).build();
    }

    @ApiOperation(value = "查询试卷详细题目，只返回题目、分值和选项")
    @GET
    @Path("showDetail")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response showExamDetail(Exam exam){
        List<Topic> topicsList = examService.getExamDetail(exam.getId(),0);
        return Response.ok(topicsList).build();
    }

    @ApiOperation(value = "查询试卷详细题目，返回题目所有内容")
    @GET
    @Path("showDetailAdmin")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response showExamDetailForAdmin(Exam exam){
        List<Topic> topicsList = examService.getExamDetail(exam.getId(),1);
        return Response.ok(topicsList).build();
    }

    @ApiOperation(value = "禁用试卷")
    @GET
    @Path("delete")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteExam(Exam exam){
        examService.deleteExam(exam.getId());
        return Response.ok("ok").build();
    }

    @ApiOperation(value = "自动生成试卷")
    @POST
    @Path("autoGenerate")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response autoGenerate(ExamModel examModel){
        Object topics = examService.autoGenerate(examModel);
        if(topics instanceof String) {
            return  Response.ok(topics).status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(topics).build();
    }
}
