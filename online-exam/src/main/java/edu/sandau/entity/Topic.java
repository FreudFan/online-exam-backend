package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * 试题
 */
@Data
public class Topic implements Serializable {
    private Integer id;

    private Integer file_id;
    private Integer type;
    private Integer difficult;
    private String description;
    private String correctkey;
    private Double topicmark;
    private String analysis;
    private Integer subject_id;
    private List<Option> optionsList;

    @JSONField(serialize = false)
    private Date createtime;
    @JSONField(serialize = false)
    private Date updatetime;
}
