package rest;

import dao.TopticsDao;
import model.TopicFile;
import model.Topics;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.servlet.WebConfig;
import service.TopticsService;
import utils.RequestUtils;
import utils.TimeUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.*;

/***
 * 文件上传
 */
@Path("topic")
public class TopicsController {

    @POST
    @Path("import")
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces({ MediaType.APPLICATION_JSON })
    public Map topicImport(@Context HttpServletRequest request) throws Exception {
        Map<String,Object> response = new HashMap<>();
        List<FileItem> fileItemList = RequestUtils.getFileItemList(request,"xlsx");
        if ( fileItemList == null || fileItemList.size() == 0 ) {
            response.put("status", "请上传文件");
            return response;
        }

        List<TopicFile> fileList = new ArrayList<>(fileItemList.size());
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
                    TopicFile topicFile = RequestUtils.saveFile(fileItem,"topics/");//将文件保存至本地
                    fileList.add(topicFile);
                }
            }
        }
        TopticsService topticsService = new TopticsService();
        topticsService.readTopicExcel(fileList);

        response.put("status", "ok");
        return response;
    }




    @GET
    @Path("show")
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces({ MediaType.APPLICATION_JSON })
    public List topicShow(@Context HttpServletRequest request) throws Exception {

        return new TopticsDao().selectTopicAll();
    }
}
