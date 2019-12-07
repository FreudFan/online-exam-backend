package service;

import dao.TopticsDao;
import model.TopicFile;
import model.Topics;
import org.springframework.stereotype.Service;
import utils.ExcelUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TopticsService {

    /***
     * 解析excel文件内容
     * @param topicFiles
     * @throws Exception
     */
    public List readTopicExcel( List<TopicFile> topicFiles ) throws Exception {
        List<List<List<Object>>> topicList = new ArrayList<>();
        for ( TopicFile topicFile: topicFiles ) {
            File file = topicFile.getFile();
            List<List<Object>> listList = ExcelUtils.readExcel(file);
            TopticsDao topticsDao = new TopticsDao();
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
        return TopticsDao.deleteTopics(idName,idArrays);
    }
}
