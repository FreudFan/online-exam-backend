package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/***
 * 做题记录
 */
@Data
@NoArgsConstructor
public class ExamRecordTopic {
    private Integer id;
    /***
     * 考试记录
     */
    private Integer recordId;
    /***
     * 题目id
     */
    private Integer topicId;
    /***
     * 做题答案
     */
    private String answer;

    @JSONField(serialize = false)
    private Date createtime;
    @JSONField(serialize = false)
    private Date updatetime;

    public ExamRecordTopic(Integer recordId, Integer topicId, String answer) {
        this.recordId = recordId;
        this.topicId = topicId;
        this.answer = answer;
    }
}
