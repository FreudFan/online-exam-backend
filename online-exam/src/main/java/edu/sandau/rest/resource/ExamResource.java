package edu.sandau.rest.resource;


import edu.sandau.entity.Exam;
import edu.sandau.security.SessionWrapper;
import edu.sandau.service.ExamService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("exam")
@Api(value = "试卷接口")
public class ExamResource {


    @Autowired
    private ExamService examService;

    /***
     *
     * @param exam
     * @return
     */
    @POST
    @Path("save")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response saveExam(Exam exam){
        examService.saveExam(exam);
        return Response.ok("ok").build();
    }

}
