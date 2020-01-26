package edu.sandau.service.impl;

import edu.sandau.dao.OptionDao;
import edu.sandau.dao.TopicDao;
import edu.sandau.dao.UploadFileDao;
import edu.sandau.entity.Option;
import edu.sandau.entity.Topic;
import edu.sandau.entity.UploadFile;
import edu.sandau.enums.DifficultTypeEnum;
import edu.sandau.enums.TopicTypeEnum;
import edu.sandau.rest.model.Page;
import edu.sandau.rest.model.TopicData;
import edu.sandau.rest.model.TopicModel;
import edu.sandau.service.SubjectService;
import edu.sandau.service.SysEnumService;
import edu.sandau.service.TopicService;
import edu.sandau.utils.ExcelUtil;
import edu.sandau.utils.FileUtil;
import edu.sandau.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicDao topicDao;
    @Autowired
    private OptionDao optionDao;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private UploadFileDao uploadFileDao;
    @Autowired
    private SysEnumService enumService;
    @Autowired
    private SubjectService subjectService;
    private final String EXCEL_TYPE = "xlsx";

    /***
     *分页查询题目方法
     * @param page
     * @return page
     */
    @Override
    public Page getTopicByPage(Page page, int flag) {
        //分页查询主表数据
        List<Topic> topics = topicDao.listTopicByPage(page, flag);
        int total = topicDao.getCount(flag);
        page.setTotal(total);
        //遍历主表数据集合,查找对应的选项数据
        topics.stream().forEach((topic) -> {
            Integer id = topic.getId();
            List<Option> optionList = optionDao.findOptionById(id);
            topic.setOptionsList(optionList);
        });
        page.setRows(this.refactorEntity(topics));
        return page;
    }

    @Override
    public TopicData readTopicExcel(InputStream fileInputStream, String fileName) throws Exception {
        //截取文件名
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!EXCEL_TYPE.equals(fileType)) {
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
        if (data == null || data.size() == 0) {
            return null;
        }
        //文件名要唯一
        fileName = fileName.substring(0, fileName.lastIndexOf(".")) + " " + TimeUtil.fileNow() + "." + fileType;
        //将文件保存至本地
        UploadFile uploadFile = fileUtil.saveFile(stream2, fileName);
        stream2.close();

        if (uploadFile == null) {
            return null;
        }
        data = this.checkTopicType(data);
        TopicData topic = new TopicData();
        topic.setFile(data);
        topic.setId(uploadFile.getId());
        return topic;
    }

    @Override
    public List<List<Object>> checkTopicType(List<List<Object>> data) {
        List<List<Object>> topics = new LinkedList<>();
        List<Object> title = data.get(0);
        title.add("题目类型");
        topics.add(title);
        int count = this.getChooseCount(title);
        for (List<Object> topic : data) {
            String index0 = topic.get(0).toString();
            if ("题目描述(必填)".equals(index0)) {
                //跳过第一行
                continue;
            }
            String index1 = topic.get(1).toString();
            String index2 = topic.get(2).toString();
            if ("对".equals(index1) && "错".equals(index2)) {
                boolean empty = true;
                for (int i = 3; i <= count; i++) {
                    if (!StringUtils.isEmpty(topic.get(i).toString())) {
                        empty = false;
                    }
                }
                if (empty) {
                    topic.add("判断题");
                } else {
                    topic.add("选择题");
                }
            } else {
                topic.add("选择题");
            }
            topics.add(topic);
        }
        return topics;
    }

    @Override
    public UploadFile getFileById(Integer id) throws Exception {
        return uploadFileDao.getFileById(id);
    }

    /***
     * 插入题目
     * @param data
     * @return
     */
    @Override
    public int save(TopicData data) {
        int id = data.getId();
        int subject_id = data.getSubject_id();
        List<List<Object>> topicList = data.getFile();
        //获得最大选项列的索引
        int options = getChooseCount(topicList.get(0));
        for (int i = 1; i < topicList.size(); i++) {
            List<Object> topic = topicList.get(i);
            Topic topicObject = new Topic();
            List<Option> optionArgs = new ArrayList<>();
            topicObject.setFile_id(id);
            topicObject.setSubject_id(subject_id);
            topicObject.setDescription(topic.get(0).toString());
            //将每行的选项单独放入一个集合
            for (int j = 1; j <= options; j++) {
                String value = topic.get(j).toString();
                if (value != null && !"".equals(value)) {
                    Option optionObject = new Option();
                    optionObject.setName(String.valueOf((char) ('A' + j - 1)));
                    optionObject.setContent(value);
                    optionArgs.add(optionObject);
                }
            }
            topicObject.setOptionsList(optionArgs);
            int size = topic.size();
            Integer type = enumService.getEnumValue("TOPIC", "TYPE", topic.get(size - 1).toString());
            topicObject.setType(type);
            Integer difficult = enumService.getEnumValue("TOPIC", "DIFFICULT", topic.get(size - 3).toString());
            topicObject.setDifficult(difficult);
            topicObject.setAnalysis(topic.get(size - 2).toString());
            topicObject.setTopicmark(((BigDecimal) topic.get(size - 4)).doubleValue());
            topicObject.setCorrectkey(topic.get(size - 5).toString());
            int keyId = topicDao.save(topicObject);
            optionDao.insertOption(keyId, optionArgs);
        }
        return 0;
    }

    /***
     * 批量插入题目
     * @param topicList
     */
    @Override
    public void insertTopics(List<Topic> topicList) {
        topicList.stream().forEach((topic) -> {
            int keyId = topicDao.save(topic);
            optionDao.insertOption(keyId, topic.getOptionsList());
        });
    }

    @Override
    public void updateTopics(Topic topic) {
        int keyId = topicDao.save(topic);
        optionDao.insertOption(keyId, topic.getOptionsList());
        topicDao.deleteTopics(topic.getId());
    }

    /***
     * 判断Excel中最多有几个选项
     * @param titleList
     * @return
     */
    @Override
    public int getChooseCount(List<Object> titleList) {
        int title = 0;
        for (int i = 1; i < titleList.size(); i++) {
            if (!titleList.get(i).toString().contains("选项")) {
                title = i - 1;
                break;
            }
        }
        return title;
    }

    @Override
    public void deleteTopics(String idName, List<Integer> idArrays) {
        topicDao.deleteTopics(idName, idArrays);
    }

    @Override
    public List<TopicModel> refactorEntity(List<Topic> topics) {
        List<TopicModel> topicModelList = new ArrayList<TopicModel>();
        topics.stream().forEach((t) -> {
            try {
                TopicModel topicModel = new TopicModel();
                BeanUtils.copyProperties(t, topicModel);
                String typeName = enumService.getEnumName("TOPIC", "TYPE", t.getType());
                String difficultName = enumService.getEnumName("TOPIC", "DIFFICULT", t.getDifficult());
                String subjectName = null;

                subjectName = subjectService.getSubjectById(t.getSubject_id()).getName();

                topicModel.setTypeName(typeName);
                topicModel.setDifficultName(difficultName);
                topicModel.setSubjectName(subjectName);
                topicModelList.add(topicModel);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
                }
        );
        return topicModelList;
    }

}
