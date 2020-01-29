package edu.sandau.rest.resource.exam;

import edu.sandau.rest.model.exam.ExamTopic;
import edu.sandau.security.Auth;
import edu.sandau.service.ExamRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Slf4j
@Path("exam/record")
@Auth
@Api(value = "考试记录")
public class ExamRecordResource {

    @Autowired
    private ExamRecordService examRecordService;

    @ApiOperation(value = "保存用户做题答案(做一道存一道)")
    @POST
    @Path("save")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Boolean saveTopic(ExamTopic examTopic) throws Exception {
        return examRecordService.saveOrUpdateTopic(examTopic);
    }

    @ApiOperation(value = "开始考试。会返回 recordId 和题目集合, recordId 需添加在后续每次保存题目的操作中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scheduleId", value = "考试日程id", dataType = "Integer", required = true )
    })
    @POST
    @Path("start")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response startExam(Map<String, Integer> param) throws Exception {
        Integer scheduleId = MapUtils.getInteger(param, "scheduleId");
        if(scheduleId == null) {
            return Response.accepted("输入参数错误").status(500).build();
        }
        Map<String, Object> params = examRecordService.startExam(scheduleId);
        return Response.ok(params).build();
    }

    @ApiOperation(value = "考试结束")
    @POST
    @Path("end")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response endExam(ExamTopic examTopic) throws Exception {
        Boolean ok = examRecordService.endExam(examTopic);
        return Response.ok(ok).build();
    }

}
