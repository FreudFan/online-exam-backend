package edu.sandau.rest.model.exam;

import edu.sandau.entity.Option;
import lombok.Data;

import java.util.List;

/***
 * 返回个前端试卷详情的实体，包含题目信息以及错题信息
 */
@Data
public class ExamDetailAndWorryTopic {
    //记录id
    private Integer record_id;
    //题目id
    private Integer topic_id;
    //题目内容
    private String description;
    //正确答案
    private String correctkey;
    //题目解析
    private String analysis;

    //题目的选项
    private List<Option> optionList;

    /***
     * 错题表数据,如回答正确则没有值
     */
    //错误答案
    private String worryanswer;

    //错误次数
    private Integer worrycount;

}
