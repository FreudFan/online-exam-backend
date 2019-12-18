package edu.sandau.service;

import edu.sandau.dao.TopticsDao;
import edu.sandau.model.UploadFile;
import edu.sandau.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TopticsService {

    @Autowired
    private TopticsDao topticsDao;

    /***
     * 解析excel文件内容
     * @param uploadFiles
     * @throws Exception
     */
    public List readTopicExcel( List<UploadFile> uploadFiles) throws Exception {
        List<List<List<Object>>> topicList = new ArrayList<>();
        for ( UploadFile uploadFile : uploadFiles) {
            File file = uploadFile.getFile();
            List<List<Object>> listList = ExcelUtil.readExcel(file);
            if ( topticsDao.insetForExcel(listList) ) {
                topicList.add(listList);
            }
        }
        return topicList;
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

    public int topicsDeleteService(String idName,String[] idArrays){
        return topticsDao.deleteTopics(idName,idArrays);
    }
}
