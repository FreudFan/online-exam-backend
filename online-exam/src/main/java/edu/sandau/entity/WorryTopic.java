package edu.sandau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/***
 * 错题
 */
@Data
public class WorryTopic {
    private Integer id;
    private Integer user_id;
    private Integer record_id;
    private Integer exam_id;
    private Integer topic_id;
    private String worryanswer;
    private String correctanswer;

    @JsonIgnore
    private Date createtime;
    @JsonIgnore
    private Date updatetime;
}
