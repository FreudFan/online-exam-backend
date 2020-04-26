package edu.sandau.rest.resource.exam;

import edu.sandau.entity.Subject;
import edu.sandau.rest.model.exam.ExamDetailAndWorryTopic;
import edu.sandau.rest.model.exam.ExamRecordAndExamDetail;
import edu.sandau.rest.model.exam.ExamTopic;
import edu.sandau.security.Auth;
import edu.sandau.security.RequestContent;
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
import java.util.List;
import java.util.Map;

@Slf4j
@Path("exam/record")
@Auth
@Api(value = "做题记录")
public class ExamRecordResource {

    @Autowired
    private ExamRecordService examRecordService;

    @ApiOperation(value = "传入examId,点击开始做题会调用此接口。会返回 recordId 和对应的题目集合, recordId 需添加在后续每次保存题目的操作中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "examId", value = "试卷Id", dataType = "Integer", required = true)
    })
    @POST
    @Path("start")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response startExam(Map<String, Integer> param) throws Exception {
        Integer examId = MapUtils.getInteger(param, "examId");
        if (examId == null) {
            return Response.accepted("输入参数错误").status(500).build();
        }
        Map<String, Object> params = examRecordService.startExam(examId);
        return Response.ok(params).build();
    }

    /***
     * 传参格式
     * {
     * 	 "recordId":14,
     * 	 "topicId":2279,
     * 	 "answer":"D"
     *
     * }
     * @param examTopic
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "保存用户做题答案(做一道存一道)")
    @POST
    @Path("save")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Boolean saveTopic(ExamTopic examTopic) throws Exception {
        return examRecordService.saveOrUpdateTopic(examTopic);
    }

    /***
     * 传参格式
     {
     "recordId":17,
     "topics":[
     {
     "topicId":2290,
     "answer":"C"
     },
     {
     "topicId":2310,
     "answer":"D"
     },
     {
     "topicId":2293,
     "answer":"B"
     },
     {
     "topicId":2295,
     "answer":"A"
     },
     {
     "topicId":2279,
     "answer":"BA"
     },
     {
     "topicId":2280,
     "answer":"C"
     },
     {
     "topicId":2282,
     "answer":"C"
     },
     {
     "topicId":2303,
     "answer":"A"
     },
     {
     "topicId":2287,
     "answer":"C"
     },
     {
     "topicId":2275,
     "answer":"C"
     }
     ]
     }
     * @param examTopic
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "点击提交做题结束")
    @POST
    @Path("end")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response endExam(ExamTopic examTopic) throws Exception {
        Double score = examRecordService.endExam(examTopic);
        return Response.ok(score).build();
    }

    @ApiOperation(value = "查看用户的所有做题记录")
    @POST
    @Path("show")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll(Map<String, Integer> params) throws Exception {
        //如果前端没有传用户id,默认使用当前登录的用户
        Integer userId = params.get("userId");
        Integer subjectId = params.get("subjectId");
        if (userId == null) {
            userId = RequestContent.getCurrentUser().getId();
        }
        List<ExamRecordAndExamDetail> examRecordAndExamDetails = examRecordService.findAll(subjectId, userId);
        return Response.ok(examRecordAndExamDetails).build();
    }

    @ApiOperation(value = "查看用户的详细做题记录")
    @POST
    @Path("showDetails")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response findDetailByRecordId(Map<String, Integer> params) throws Exception {
        Integer recordId = params.get("recordId");
        List<ExamDetailAndWorryTopic> examDetailAndWorryTopic = examRecordService.findDetailByRecordId(recordId);
        return Response.ok(examDetailAndWorryTopic).build();
    }

    @ApiOperation(value = "查询用户做过哪些课程的试卷")
    @POST
    @Path("showSubject")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getSubjectByUserId(Map<String, Integer> params) throws Exception {
        Integer userId = MapUtils.getInteger(params, "userId", null);
        if (userId == null) {
            userId = RequestContent.getCurrentUser().getId();
        }
        List<Subject> subjects = examRecordService.getSubjectByUserId(userId);
        return Response.ok(subjects).build();
    }

}
