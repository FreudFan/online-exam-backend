package edu.sandau.service.impl;

import edu.sandau.dao.ExamDao;
import edu.sandau.dao.TopicDao;
import edu.sandau.entity.Exam;
import edu.sandau.entity.ExamDetail;
import edu.sandau.entity.Topic;
import edu.sandau.rest.model.Page;
import edu.sandau.rest.model.exam.ExamClazz;
import edu.sandau.rest.model.exam.ExamModel;
import edu.sandau.service.ExamService;
import edu.sandau.service.SysEnumService;
import edu.sandau.service.TopicService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ExamServiceImpl implements ExamService  {

    @Autowired
    private ExamDao examDao;
    @Autowired
    private TopicDao topicDao;
    @Autowired
    private TopicService topicService;
    @Autowired
    private SysEnumService sysEnumService;
    @Override
    public void saveExam(Exam exam) {
        exam = examDao.save(exam);
        examDao.saveDetail(exam);
    }

    @Override
    public Page getExamByPage(Page page, int flag) {
        List<Exam> exams = examDao.listExamByPage(page,flag);
        int total = examDao.getCount(flag);
        page.setTotal(total);
        page.setRows(exams);
        return page;
    }

    @Override
    public List<Topic> getExamDetail(Integer id, Integer role) {
        List<ExamDetail> examDetails = examDao.listExamDetail(id);
        List<Integer> ids = new ArrayList<Integer>();
        examDetails.stream().forEach((examDetail)->{
            ids.add(examDetail.getTopic_id());
        });
        List<Topic> topics = topicService.getTopicById(ids,role);
        int i = 0;
        topics.stream().forEach((topic)->{
            for (ExamDetail ed : examDetails)  {
                if(ed.getTopic_id().intValue() == topic.getId().intValue()){
                    topic.setTopicmark(ed.getTopicmark());
                    break;
                }
            }
        });
        return topics;
    }

    @Override
    public void deleteExam(Integer id) {
        examDao.deleteExam(id);
    }

    @Override
    public Object autoGenerate(ExamModel examModel) {
        List<Topic> topicList = new ArrayList<>();
        int subjectId = examModel.getSubjectId();
        List<ExamClazz> clazzList = examModel.getClazz();
        for ( ExamClazz clazz: clazzList ) {
            int type = clazz.getType();
            int difficult = clazz.getDifficult();
            List<Topic> topics = topicDao.getTopicByTypeAndDifficult(subjectId, type, difficult);
            if (topics.size() < clazz.getNum()) {
                String difficultName = sysEnumService.getEnumName("TOPIC", "DIFFICULT", difficult);
                String typeName = sysEnumService.getEnumName("TOPIC", "TYPE", type);
                return "题库中难度 “" + difficultName + "” 的 “" + typeName + "” 数量不足，请新增题目。";
            }
            topics = randomTopic(topics, clazz.getNum(), clazz.getScore());
            topics = topicService.getTopicsDetail(topics);
            topicList.addAll(topics);
        }
        return topicList;
    }

    /***
     * 随意抽取题目，通过随机生成下标，从list里随机拿数据
     * @param topicList
     * @param num 随机生成题目总数
     * @param totalScore 当前题目总分
     * @return
     */
    private List<Topic> randomTopic(List<Topic> topicList, Integer num, Double totalScore) {
        BigDecimal bg = new BigDecimal(totalScore/num);
        Double score = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        Set<Integer> topicIds = new HashSet<>(num);
        while(topicIds.size() != num) {
            int index = RandomUtils.nextInt(0, topicList.size());
            topicIds.add(index);
        }
        List<Topic> topics = new ArrayList<>(num);
        for(Integer index: topicIds) {
            Topic topic = topicList.get(index);
            topic.setTopicmark(score);
            topics.add(topic);
        }
        return topics;
    }
}
