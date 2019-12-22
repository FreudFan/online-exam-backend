package edu.sandau.rest;

import edu.sandau.security.Auth;
import edu.sandau.dao.TopticsDao;
import edu.sandau.model.UploadFile;
import edu.sandau.service.TopticsService;
import edu.sandau.utils.FileUtil;
import org.apache.commons.fileupload.FileItem;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/***
 * 题库
 */
@Path("topic")
@Auth
public class TopicsController {

    @Autowired
    private TopticsDao topticsDao;
    @Autowired
    private TopticsService topticsService;
    @Autowired
    private FileUtil fileUtil;
    @Context
    private SecurityContext securityContext;

    @Deprecated
    @POST
    @Path("@import")
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces({ MediaType.APPLICATION_JSON })   //此方法以弃用
    public Map topicImport(@Context HttpServletRequest request) throws Exception {
        Map<String,Object> response = new HashMap<>();
        List<FileItem> fileItemList = fileUtil.getFileItemList(request,"xlsx");
        if ( fileItemList == null || fileItemList.size() == 0 ) {
            response.put("status", "请上传文件");
            return response;
        }

        List<UploadFile> fileList = new ArrayList<>(fileItemList.size());
        for (FileItem fileItem : fileItemList) {
            //判断是否是普通字段
            if ( !fileItem.isFormField() )  {
                String fileName = fileItem.getName();
                if ( fileName != null && !fileName.equals("") ) {
                    //截取文件名
                    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if ( !fileType.equals("xlsx") ) {
                        response.put("status", "请上传xlsx格式文件");
                        return response;
                    }
                    UploadFile uploadFile = fileUtil.saveFile(fileItem, "topics/");//将文件保存至本地
                    fileList.add(uploadFile);
                }
            }
        }
        List topicList = topticsService.saveTopicExcel(fileList);

        response.put("topics", topicList);
        return response;
    }

    @POST
    @Path("import")
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces({ MediaType.APPLICATION_JSON })
    public ResponseEntity topic(@FormDataParam("file") InputStream fileInputStream,
                                     @FormDataParam("file") FormDataContentDisposition disposition) throws Exception {
        String fileName = new String(disposition.getFileName().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        int userId = Integer.parseInt(securityContext.getUserPrincipal().getName());
        List data = topticsService.readTopicExcel(fileInputStream, fileName, userId);
        if ( data == null ) {
            return new ResponseEntity("请上传xlsx格式文件", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity(data, HttpStatus.OK);
    }


    @GET
    @Path("show")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public List topicShow() throws Exception {
        List<Map<String, Object>> topicsList = topticsDao.selectTopicAll();
        List<Map<String, Object>> showList = new ArrayList<>();
        Map<String, Object> showMap = null;
        int count ;
        if(!topicsList.isEmpty()){
            for (int i = 0; i < topicsList.size(); i+=count) {
                count = 1;
                Map<String, Object> tempMap = new HashMap<>();
                tempMap = topicsList.get(i);
                String option = tempMap.get("option").toString();
                String value = tempMap.get("value").toString();
                showMap = new LinkedHashMap<>();
                showMap.put("topics_id", tempMap.get("topics_id"));
                showMap.put("description", tempMap.get("description"));
                showMap.put("correctkey", tempMap.get("correctkey"));
                showMap.put("topicmark", tempMap.get("topicmark"));
                showMap.put("analysis", tempMap.get("analysis"));
                showMap.put(option,value);
                for (int j = i + 1; j < topicsList.size(); j++) {

                    if(!topicsList.get(j).get("topics_id").toString().equals(topicsList.get(i).get("topics_id").toString()))
                    {
                       break;
                    }
                    showMap.put(topicsList.get(j).get("option").toString(),topicsList.get(j).get("value").toString());
                    count ++ ;
                }
                showList.add(showMap);

            }
        }
        return showList;
    }


    @POST
    @Path("delete")
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @Produces({ MediaType.APPLICATION_JSON })
    public String topicDelete(MultivaluedMap topicsMap) {
//        String[] idArrays = topicsMap.get("topics_id").substring(1, topicsMap.get("topics_id").length() - 1).split(",");
        List topicsList = (List)topicsMap.get("topics_id");
        String id = (String)topicsList.get(0);
        String[] idArrays = id.split(",");
        int count = topticsService.topicsDeleteService("topics_id",idArrays);
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
        String[] idArrays = topicsMap.get("topics_id").substring(1,topicsMap.get("topics_id").length()-1).split(",");
        int count = topticsService.topicsDeleteService("topics_id",idArrays);
        if(count > 0){
            return "success";
        }else{
            return "fail";
        }
    }
}
