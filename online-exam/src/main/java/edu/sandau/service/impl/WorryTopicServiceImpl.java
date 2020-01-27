package edu.sandau.service.impl;


import edu.sandau.dao.WorryTopicDao;
import edu.sandau.entity.WorryTopic;
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


    @Override
    public void saveWorryTopic(List<WorryTopic> worryTopicList) {
        worryTopicDao.saveWorryTopic(worryTopicList);
    }
}
