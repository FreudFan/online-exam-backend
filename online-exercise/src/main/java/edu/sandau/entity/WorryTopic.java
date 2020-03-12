package edu.sandau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/***
 * 错题
 */
@Data
public class WorryTopic {
    //错题id
    private Integer id;
    //用户id
    private Integer user_id;
    //考试记录表id
    private Integer record_id;
    //考试id
    private Integer exam_id;
    //题目id
    private Integer topic_id;
    //错误答案
    private String worryanswer;
    //正确答案
    private String correctanswer;
    //错误次数
    private Integer worrycount;
    @JsonIgnore
    private Date createtime;
    @JsonIgnore
    private Date updatetime;
}
