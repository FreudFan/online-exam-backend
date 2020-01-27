package edu.sandau.service.impl;

import edu.sandau.dao.ExamRecordDao;
import edu.sandau.rest.model.exam.ExamTopic;
import edu.sandau.service.ExamRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExamRecordServiceImpl implements ExamRecordService {
    @Autowired
    private ExamRecordDao examRecordDao;

    @Override
    public Boolean saveTopic(ExamTopic examTopic) throws Exception {
        return null;
    }

}
