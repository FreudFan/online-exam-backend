package edu.sandau.rest.resource;

import edu.sandau.entity.WorryTopicAnalysis;
import edu.sandau.rest.model.Page;
import edu.sandau.security.Auth;
import edu.sandau.service.WorryTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Slf4j
@Path("worryTopic")
@Api(value = "错题接口")
@Auth
public class WorryTopicResource {
    @Autowired
    private WorryTopicService worryTopicService;

    /***
     * 传参格式
     * {
     * 	"pageNo":1,
     * 	"pageSize":3,
     * 	"option":{
     * 		"user_id":1,
     * 		"subject_id":2,
     * 		"record_id":3
     *        }
     * }
     */
    @ApiOperation(value = "查询错题")
    @POST
    @Path("show")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response show(Page page) {
        if (page == null) {
            page = new Page();
        }
        page = worryTopicService.getWorryTopicByPage(page);
        return Response.ok(page).build();
    }

    @ApiOperation(value = "错题数据分析统计")
    @POST
    @Path("analysis")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response analysisTopic(Map<String,String> params) {
        List<WorryTopicAnalysis> worryTopicAnalysis= worryTopicService.getWorryTopicAnalysis(params.get("id"),params.get("correctkey"));
        return Response.ok(worryTopicAnalysis).build();
    }
}
