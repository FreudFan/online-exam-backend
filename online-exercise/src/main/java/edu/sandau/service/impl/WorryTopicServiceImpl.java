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
        //存入错题表之前先看之前有没有错过这题，错过就修改错误次数,没有存入错题表
        worryTopicList.stream().forEach((worryTopic) -> {
            WorryTopic wt = findWorryTopicByUserIdAndTopicId(worryTopic.getUser_id(), worryTopic.getTopic_id());
            if (wt == null) {
                worryTopicDao.saveWorryTopic(worryTopic);
            } else {
                worryTopicDao.updateWorryCount(wt);
            }
        });

    }

    @Override
    public WorryTopic findWorryTopicByUserIdAndTopicId(Integer userId, Integer topicId) {
        List<WorryTopic> worryTopic = worryTopicDao.findById(userId, topicId);
        if (worryTopic != null && worryTopic.size() > 0) {
            return worryTopic.get(0);
        } else {
            return null;
        }

    }

    @Override
    public void deleteWorryTopicByRecordId(Integer RecordId, Integer userId) {
        worryTopicDao.deleteById(RecordId, userId);
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
                String subjectName = subjectService.getSubjectById(worryTopic.getSubject_id()).getName();
                worryTopic.setDifficultName(difficultName);
                worryTopic.setSubjectName(subjectName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        page.setRows(worryTopicList);
        return page;
    }

    @Override
    public WorryTopic findWorryTopicByRecordId(Integer record_id, Integer topic_id) {
        return worryTopicDao.findWorryTopicByRecordId(record_id, topic_id);
    }
}
