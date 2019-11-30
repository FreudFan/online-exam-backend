package rest;

import model.TopicFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.servlet.WebConfig;
import service.TopticsService;
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
        TopticsService topticsService = new TopticsService();
        request.setCharacterEncoding("UTF-8");
        String contentType = request.getContentType();
        if ((contentType.indexOf("multipart/form-data") >= 0)) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            File f = new File("topics"); //新建保存目录
            if ( !f.exists() ) {
                f.mkdir();
            }

            // 创建一个新的文件上传处理程序
            ServletFileUpload fileUpload = new ServletFileUpload(factory);
            fileUpload.setHeaderEncoding("utf-8");
            List<FileItem> fileItemList = fileUpload.parseRequest(request);

            List<TopicFile> fileList = new ArrayList<>(fileItemList.size());
            for (FileItem fileItem : fileItemList) {
                //判断是否是普通字段
                if ( !fileItem.isFormField() )  {
                    String fileName = fileItem.getName();
                    if ( fileName != null && !fileName.equals("") ) {
                        //截取文件名
                        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                        //文件名要唯一
                        fileName = fileName.substring(0,fileName.lastIndexOf(".")) + " " + TimeUtils.fileNow();
//                        fileName = fileName.replace(" ", "_");
                        //保存在服务器端
                        String filePath = "topics/" + fileName + "." + fileType;

                        File file = new File(filePath);
                        try(
                                InputStream inputStream = fileItem.getInputStream();
                                FileOutputStream outputStream = new FileOutputStream(file);
                                ) {
                            // 流的对拷
                            byte[] buffer = new byte[1024]; //每次读取1个字节
                            int len;
                            // 开始读取上传文件的字节，并将其输出到服务端的撒花姑娘穿文件的输出流中
                            while ( ( len = inputStream.read(buffer) ) > 0 ) {
                                outputStream.write(buffer, 0, len);
                            }

                            TopicFile topicFile = new TopicFile();
                            topicFile.setFilePath(filePath);
                            topicFile.setFile(file);
                            fileList.add(topicFile);
                        } catch ( Exception e ) {
                            e.printStackTrace();
                        } finally {
                            fileItem.delete();
                        }
                    }
                }
            }
            topticsService.readTopicExcel(fileList);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("status", "测试中文123abc");
        return map;
    }

}
