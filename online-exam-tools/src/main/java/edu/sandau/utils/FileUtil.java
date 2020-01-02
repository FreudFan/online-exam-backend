package edu.sandau.utils;

import edu.sandau.dao.UploadFileDao;
import edu.sandau.model.UploadFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

@Component
public class FileUtil {

    private final static String UPLOAD_DIR = "files";

    static {
        //新建保存目录
        File f = new File(UPLOAD_DIR);
        if ( !f.exists() ) {
            f.mkdir();
        }
    }
    @Autowired
    private UploadFileDao uploadFileDao;

    /***
     * 把request请求转成文件对象列表，
     * 需指定文件格式用来设置本地保存目录
     * @param request   request请求
     * @param fileType  文件格式
     * @return
     * @throws UnsupportedEncodingException
     * @throws FileUploadException
     */
    public List<FileItem> getFileItemList( HttpServletRequest request, String fileType )
            throws UnsupportedEncodingException, FileUploadException {
        request.setCharacterEncoding("UTF-8");
        String contentType = request.getContentType();
        if ((contentType.contains("multipart/form-data"))) {
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
    public UploadFile saveFile(FileItem fileItem, String filePath) {
        String fileName = fileItem.getName();   //文件名
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);    //文件类型
        //文件名要唯一
        fileName = fileName.substring(0,fileName.lastIndexOf(".")) + " " + TimeUtil.fileNow();
        //保存在服务器端
        filePath = filePath + fileName + "." + fileType;

        File file = new File(filePath);
        UploadFile uploadFile = new UploadFile();
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
            uploadFile.setFile(file);
            uploadFile.setFilePath(filePath);
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            fileItem.delete();
        }
        return uploadFile;
    }

    public UploadFile saveFile(InputStream inputStream, String fileName, Integer userId) {
        File file = new File(UPLOAD_DIR,fileName);
        UploadFile uploadFile = new UploadFile();
        try(
                FileOutputStream outputStream = new FileOutputStream(file)
        ) {
            byte[] buffer = new byte[1024]; //每次读取1个字节
            int len;
            // 开始读取上传文件的字节，并将其输出到服务端的撒花姑娘穿文件的输出流中
            while ( ( len = inputStream.read(buffer) ) > 0 ) {
                outputStream.write(buffer, 0, len);
            }
            uploadFile.setFileName(fileName);
            uploadFile.setFile(file);
            uploadFile.setFilePath(UPLOAD_DIR + File.separatorChar + fileName);
            uploadFile.setUser_id(userId);
            uploadFileDao.save(uploadFile);
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
        return uploadFile;
    }

    /***
     * 复制流
     * @param input
     * @return
     */
    public ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
