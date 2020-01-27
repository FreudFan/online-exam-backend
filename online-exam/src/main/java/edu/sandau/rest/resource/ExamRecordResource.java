package edu.sandau.rest.resource;

import edu.sandau.entity.Exam;
import edu.sandau.rest.model.exam.ExamTopic;
import edu.sandau.security.Auth;
import edu.sandau.service.ExamRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("exam/record")
@Auth
@Api(value = "考试记录")
public class ExamRecordResource {

    @Autowired
    private ExamRecordService examRecordService;

    @ApiOperation(value = "保存用户做题答案(做一道存一道)")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Boolean saveTopic(ExamTopic examTopic) throws Exception {
        return examRecordService.saveTopic(examTopic);
    }

    @ApiOperation(value = "开始考试")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response startExam() throws Exception {
        return null;
    }

}
