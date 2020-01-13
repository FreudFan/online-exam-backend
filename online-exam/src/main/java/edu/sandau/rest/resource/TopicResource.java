package edu.sandau.rest.resource;

import edu.sandau.entity.UploadFile;
import edu.sandau.security.Auth;
import edu.sandau.dao.TopticsDao;
import edu.sandau.security.SessionWrapper;
import edu.sandau.service.TopticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/***
 * 题库
 */
@Path("topic")
@Api(value = "题库接口")
@Auth
public class TopicResource {

    @Autowired
    private TopticsDao topticsDao;
    @Autowired
    private TopticService topticsService;
    @Context
    private SecurityContext securityContext;
    @Autowired
    private SessionWrapper sessionWrapper;

    @POST
    @Path("import")
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "导入题库", response = Response.class)
    public Response topic(@FormDataParam("file") InputStream fileInputStream,
                          @FormDataParam("file") FormDataContentDisposition disposition) throws Exception {
        if ( fileInputStream == null || disposition == null ) {
            return Response.accepted("请上传xlsx格式文件").status(Response.Status.BAD_REQUEST).build();
        }
        String fileName = new String(disposition.getFileName()
                .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//        int userId = Integer.parseInt(securityContext.getUserPrincipal().getName());
        Map<String, Object> data = topticsService.readTopicExcel(fileInputStream, fileName);
        if ( data == null ) {
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
    @GET
    @Path("download")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@QueryParam("fid") Integer fid, @Context HttpHeaders httpHeaders) throws Exception {
        UploadFile uploadFile = topticsService.getFileById(fid);
        File f = new File(uploadFile.getFilePath());
        String fileName = uploadFile.getFileName();
        if (!f.exists()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            String agent = httpHeaders.getHeaderString("USER-AGENT");
            //需要对文件名进行编码，否则会乱码 火狐浏览器下载文件需单独处理文件编码
            if( agent != null && agent.toLowerCase().indexOf("firefox") > 0 ) {
                fileName = "=?UTF-8?B?" + Base64.getEncoder().encodeToString(fileName.getBytes(StandardCharsets.UTF_8)) + "?=";
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
            return Response.ok(f).header("Content-disposition", "attachment;filename=" + fileName)
                    .header("Cache-Control", "no-cache").build();
        }
    }

    @GET
    @Path("show")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response topicShow() throws Exception {
        List<Map<String, Object>> topicsList = topticsDao.selectTopicAll();
        List<Map<String, Object>> showList = new ArrayList<>();
        Map<String, Object> showMap = null;
        int count ;
        if(!topicsList.isEmpty()){
            for (int i = 0; i < topicsList.size(); i+=count) {
                count = 1;
                Map<String, Object> tempMap = topicsList.get(i);
                String option = tempMap.get("option").toString();
                String value = tempMap.get("value").toString();
                showMap = new LinkedHashMap<>();
                showMap.put("id", tempMap.get("id"));
                showMap.put("description", tempMap.get("description"));
                showMap.put("correctkey", tempMap.get("correctkey"));
                showMap.put("topicmark", tempMap.get("topicmark"));
                showMap.put("analysis", tempMap.get("analysis"));
                showMap.put(option,value);
                for (int j = i + 1; j < topicsList.size(); j++) {

                    if(!topicsList.get(j).get("id").toString().equals(topicsList.get(i).get("id").toString()))
                    {
                       break;
                    }
                    showMap.put(topicsList.get(j).get("option").toString(),topicsList.get(j).get("value").toString());
                    count ++ ;
                }
                showList.add(showMap);

            }
        }
        return Response.accepted(showList).build();
    }

    @POST
    @Path("delete")
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @Produces({ MediaType.APPLICATION_JSON })
    public String topicDelete(MultivaluedMap topicsMap) {
//        String[] idArrays = topicsMap.get("id").substring(1, topicsMap.get("id").length() - 1).split(",");
        List topicsList = (List)topicsMap.get("id");
        String id = (String)topicsList.get(0);
        String[] idArrays = id.split(",");
        int count = topticsService.deleteTopics("id",idArrays);
        if(count > 0){
            return "success";
        }else{
            return "fail";
        }
    }

    @POST
    @Path("deleteJSON")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String topicDelete_JSON( Map<String,String> topicsMap)  {
        String[] idArrays = topicsMap.get("id").substring(1,topicsMap.get("id").length()-1).split(",");
        int count = topticsService.deleteTopics("id",idArrays);
        if(count > 0){
            return "success";
        }else{
            return "fail";
        }
    }

}
