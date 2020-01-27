package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
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

    @JSONField(serialize = false)
    private Date createtime;
    @JSONField(serialize = false)
    private Date updatetime;
}
