package utils;

import model.TopicFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import service.TopticsService;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;


public class RequestUtils {

    /***
     * 把request请求转成文件对象列表，
     * 需指定文件格式用来设置本地保存目录
     * @param request   request请求
     * @param fileType  文件格式
     * @return
     * @throws UnsupportedEncodingException
     * @throws FileUploadException
     */
    public static List<FileItem> getFileItemList( HttpServletRequest request, String fileType ) throws UnsupportedEncodingException, FileUploadException {
        request.setCharacterEncoding("UTF-8");
        String contentType = request.getContentType();
        if ((contentType.contains("multipart/form-data"))) {
            if ( fileType.equals("xlsx") ) {
                File f = new File("topics"); //新建保存目录
                if ( !f.exists() ) f.mkdir();
            }

            // 创建一个新的文件上传处理程序
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload fileUpload = new ServletFileUpload(factory);
            fileUpload.setHeaderEncoding("utf-8");
            List<FileItem> fileItemList = fileUpload.parseRequest(request);

            if ( fileItemList.size() > 0 ) {
                return fileItemList;
            }
        }
        return null;
    }

    /***
     * 将上传的文件保存至本地
     * @param fileItem  文件对象
     * @param filePath  文件路径，以'/'结尾
     * @return
     */
    public synchronized static TopicFile saveFile(FileItem fileItem, String filePath) {
        String fileName = fileItem.getName();   //文件名
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);    //文件类型
        //文件名要唯一
        fileName = fileName.substring(0,fileName.lastIndexOf(".")) + " " + TimeUtils.fileNow();
        //保存在服务器端
        filePath = filePath + fileName + "." + fileType;

        File file = new File(filePath);
        TopicFile topicFile = new TopicFile();
        try(
                InputStream inputStream = fileItem.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(file)
        ) {
            // 流的对拷
            byte[] buffer = new byte[1024]; //每次读取1个字节
            int len;
            // 开始读取上传文件的字节，并将其输出到服务端的撒花姑娘穿文件的输出流中
            while ( ( len = inputStream.read(buffer) ) > 0 ) {
                outputStream.write(buffer, 0, len);
            }

            topicFile.setFilePath(filePath);
            topicFile.setFile(file);
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            fileItem.delete();
        }
        return topicFile;
    }

}
