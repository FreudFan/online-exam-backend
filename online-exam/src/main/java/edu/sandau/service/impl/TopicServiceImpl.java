package edu.sandau.service.impl;

import edu.sandau.dao.TopicDao;
import edu.sandau.dao.UploadFileDao;
import edu.sandau.entity.UploadFile;
import edu.sandau.rest.model.TopicData;
import edu.sandau.service.TopicService;
import edu.sandau.utils.ExcelUtil;
import edu.sandau.utils.FileUtil;
import edu.sandau.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicDao topicDao;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private UploadFileDao uploadFileDao;

    private final String excel_type = "xlsx";

    public Map<String, Object> readTopicExcel(InputStream fileInputStream, String fileName) throws Exception {
        //截取文件名
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        if ( !excel_type.equals(fileType) ) {
            return null;
        }

        //需将流克隆成两个流才可进行读和谐操作，读和写操作会使流数据被写完而读不到数据
        //思路：先把InputStream转化成ByteArrayOutputStream  后面要使用InputStream对象时，再从ByteArrayOutputStream转化回来
        ByteArrayOutputStream baos = fileUtil.cloneInputStream(fileInputStream);
        fileInputStream.close();
        // 打开两个新的输入流
        assert baos != null;
        InputStream stream1 = new ByteArrayInputStream(baos.toByteArray());
        InputStream stream2 = new ByteArrayInputStream(baos.toByteArray());

        List<List<Object>> data = ExcelUtil.readExcel(stream1);
        stream1.close();
        if ( data == null || data.size() == 0 ) {
            return null;
        }
        //文件名要唯一
        fileName = fileName.substring(0,fileName.lastIndexOf(".")) + " " + TimeUtil.fileNow() + "." + fileType;
        //将文件保存至本地
        UploadFile uploadFile = fileUtil.saveFile(stream2, fileName);
        stream2.close();

        if ( uploadFile == null ) {
            return null;
        }
        Map<String, Object> topic = new HashMap<>(2);
        topic.put("file", data);
        topic.put("id", uploadFile.getId());
        return topic;
    }

    public UploadFile getFileById(Integer id) throws Exception {
        return uploadFileDao.getFileById(id);
    }

    public List<List<List<Object>>> saveTopicExcel(List<UploadFile> uploadFiles) throws Exception {
        List<List<List<Object>>> topicList = new ArrayList<>();
        for ( UploadFile uploadFile : uploadFiles) {
            File file = uploadFile.getFile();
            List<List<Object>> listList = ExcelUtil.readExcel(file);
            if ( topicDao.insetForExcel(listList) ) {
                topicList.add(listList);
            }
        }
        return topicList;
    }

    public int save(TopicData data){
       int count = topicDao.saveTopics(data);
       return count;
    }


    public int getChooseCount( List<Object> titleList)  {
        int title = 0;
        for (int i = 1; i < titleList.size(); i++) {
            if(!titleList.get(i).toString().contains("选项")){
                title = i-1;
                break;
            }
        }
        return title;
    }

    public int deleteTopics(String idName, String[] idArrays){
        return topicDao.deleteTopics(idName,idArrays);
    }
}
