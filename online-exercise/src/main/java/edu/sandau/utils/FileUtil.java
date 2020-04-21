package edu.sandau.utils;

import edu.sandau.dao.UploadFileDao;
import edu.sandau.entity.UploadFile;
import edu.sandau.security.RequestContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileUtil {

    private final static String UPLOAD_DIR = "files";

    static {
        //新建保存目录
        File f = new File(UPLOAD_DIR);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    @Autowired
    private UploadFileDao uploadFileDao;

    public UploadFile saveFile(InputStream inputStream, String fileName) {
        File file = new File(UPLOAD_DIR, fileName);
        UploadFile uploadFile = new UploadFile();
        try (
                FileOutputStream outputStream = new FileOutputStream(file)
        ) {
            //每次读取1个字节
            byte[] buffer = new byte[1024];
            int len;
            // 开始读取上传文件的字节，并将其输出到服务端的撒花姑娘穿文件的输出流中
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            uploadFile.setFileName(fileName);
            uploadFile.setFile(file);
            uploadFile.setFilePath(UPLOAD_DIR + File.separatorChar + fileName);
            Integer userId = RequestContent.getCurrentUser().getId();
            uploadFile.setUser_id(userId);
            uploadFileDao.save(uploadFile);
        } catch (Exception e) {
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
