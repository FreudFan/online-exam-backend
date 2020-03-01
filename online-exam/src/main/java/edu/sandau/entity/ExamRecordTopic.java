package edu.sandau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private Date createtime;
    @JsonIgnore
    private Date updatetime;

    public ExamRecordTopic(Integer recordId, Integer topicId, String answer) {
        this.recordId = recordId;
        this.topicId = topicId;
        this.answer = answer;
    }
}
