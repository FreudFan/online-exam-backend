package edu.sandau.service.impl;


import edu.sandau.dao.OptionDao;
import edu.sandau.dao.WorryTopicDao;
import edu.sandau.entity.Option;
import edu.sandau.entity.WorryTopic;
import edu.sandau.rest.model.Page;
import edu.sandau.rest.model.WorryTopicData;
import edu.sandau.service.SubjectService;
import edu.sandau.service.SysEnumService;
import edu.sandau.service.WorryTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WorryTopicServiceImpl implements WorryTopicService {

    @Autowired
    private WorryTopicDao worryTopicDao;
    @Autowired
    private OptionDao optionDao;
    @Autowired
    private SysEnumService enumService;
    @Autowired
    private SubjectService subjectService;

    @Override
    public void saveWorryTopic(List<WorryTopic> worryTopicList) {
        //考虑考试答案错误情况，插入错题记录之前先进行一次判断
        findWorryTopicByRecordId(worryTopicList.get(0).getRecord_id(),worryTopicList.get(0).getUser_id());
        worryTopicDao.saveWorryTopic(worryTopicList);
    }

    @Override
    public void findWorryTopicByRecordId(Integer RecordId,Integer userId) {
        List<WorryTopic> worryTopics = worryTopicDao.findById(RecordId, userId);
        if(worryTopics != null && worryTopics.size() > 0){
            deleteWorryTopicByRecordId(RecordId,userId);
        }
    }

    @Override
    public void deleteWorryTopicByRecordId(Integer RecordId,Integer userId) {
        worryTopicDao.deleteById(RecordId,userId);
    }

    @Override
    public Page getWorryTopicByPage(Page page) {
        List<WorryTopicData> worryTopicList = worryTopicDao.selectAll(page);
        worryTopicList.stream().forEach((worryTopic) -> {
            try {
                Integer id = worryTopic.getId();
                List<Option> optionList = optionDao.findOptionById(id);
                worryTopic.setOptionList(optionList);
                String difficultName = enumService.getEnumName("TOPIC", "DIFFICULT", worryTopic.getDifficult());
                worryTopic.setDifficultName(difficultName);
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        page.setRows(worryTopicList);
        return page;
    }
}
