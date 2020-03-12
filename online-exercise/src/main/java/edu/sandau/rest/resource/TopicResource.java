package edu.sandau.rest.resource;

import edu.sandau.entity.Topic;
import edu.sandau.entity.UploadFile;
import edu.sandau.rest.model.Page;
import edu.sandau.rest.model.TopicData;
import edu.sandau.security.Auth;
import edu.sandau.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/***
 * 题库
 */
@Path("topic")
@Api(value = "题库接口")
@Auth
public class TopicResource {

    @Autowired
    private TopicService topicService;

    @ApiOperation(value = "导入题库", response = Map.class)
    @POST
    @Path("import")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.APPLICATION_JSON})
    public Response topic(@FormDataParam("file") InputStream fileInputStream,
                          @FormDataParam("file") FormDataContentDisposition disposition) throws Exception {
        if (fileInputStream == null || disposition == null) {
            return Response.accepted("请上传xlsx格式文件").status(Response.Status.BAD_REQUEST).build();
        }
        String fileName = new String(disposition.getFileName()
                .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        TopicData data = topicService.readTopicExcel(fileInputStream, fileName);
        if (data == null) {
            return Response.ok("请上传xlsx格式文件").status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(data).build();
    }

    /***
     * 下载用户上传的文件
     * @param fid 文件id
     * @param httpHeaders
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "下载用户上传的文件", response = InputStream.class)
    @GET
    @Path("download")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@QueryParam("fid") Integer fid, @Context HttpHeaders httpHeaders) throws Exception {
        UploadFile uploadFile = topicService.getFileById(fid);
        File f = new File(uploadFile.getFilePath());
        String fileName = uploadFile.getFileName();
        if (!f.exists()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            String agent = httpHeaders.getHeaderString("USER-AGENT");
            //需要对文件名进行编码，否则会乱码 火狐浏览器下载文件需单独处理文件编码
            if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
                fileName = "=?UTF-8?B?" + Base64.getEncoder().encodeToString(fileName.getBytes(StandardCharsets.UTF_8)) + "?=";
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
            return Response.ok(f).header("Content-disposition", "attachment;filename=" + fileName)
                    .header("Cache-Control", "no-cache").build();
        }
    }

    /**
     * @param page 分页对象
     * @return 分页数据
     * @throws Exception
     */
    @ApiOperation(value = "分页查询题目", response = Map.class)
    @GET
    @Path("show")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response topicShow(Page page) throws Exception {
        if (page == null) {
            page = new Page();
        }
        page = topicService.getTopicByPage(page, 1);
        return Response.ok(page).build();
    }

    /***
     *
     * @param data
     * @return 插入成功
     * @throws Exception
     */
    @ApiOperation(value = "保存上传的文件")
    @POST
    @Path("save")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response topicSave(TopicData data) throws Exception {
        topicService.save(data);

        return Response.accepted("ok").build();
    }


    /***
     * 传参格式:
     * {
     * [2091,2092,2093]
     * }
     * @param
     * @return删除成功
     */
    @ApiOperation(value = "批量禁用题目功能")
    @POST
    @Path("delete")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response topicDelete_JSON(Integer... ids) {
        topicService.deleteTopics(ids);
        return Response.ok("ok").build();
    }

    /***
     * json格式
     *      	[
     *           {
     * 			"type":1,
     * 			"difficult":1,
     * 			"description":"test",
     * 			"correctkey":"B",
     * 			"topicmark":20.0,
     * 			"analysis":"test",
     * 			"subject_id":2,
     * 			"optionsList":[
     *            {"name":"A","content":"test"},
     *            {"name":"B","content":"test2"},
     *            {"name":"C","content":"test3"},
     *            {"name":"D","content":"test4"}
     * 			]
     *        }
     *     	]
     *批量插入题目
     * @param topicList
     * @return插入成功
     */
    @ApiOperation(value = "插入用户自定义题目")
    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response topicInsert(List<Topic> topicList) {
        topicService.insertTopics(topicList);
        return Response.ok("ok").build();
    }

    /***
     * 传参方式
     * {
     * 	"id":1000,
     * 	 "topics":"test1",
     * 	 "type":1,
     * 	 "difficult":1,
     * 	 "description":"测试更新",
     * 	 "correctkey":"B",
     * 	 "topicmark":20.5,
     * 	 "analysis":"测试更新",
     * 	 "subject_id":3,
     * 	 "optionsList":[
     *         {"name":"A","content":"test"},
     *        {"name":"B","content":"testB"},
     *        {"name":"C","content":"testC"},
     *        {"name":"D","content":"testD"}
     * 	 	]
     *
     * }
     * 更新试题
     * @param topic
     * @return
     */
    @ApiOperation(value = "更新题目")
    @POST
    @Path("update")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response topicUpdate(Topic topic) {
        topicService.updateTopics(topic);
        return Response.ok("ok").build();
    }


    @ApiOperation(value = "查询已禁用的题目")
    @GET
    @Path("showDelete")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteTopicShow(Page page) {
        if (page == null) {
            page = new Page();
        }
        page = topicService.getTopicByPage(page, 0);
        return Response.ok(page).build();
    }
}
