package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * 试卷
 */
@Data
public class Exam implements Serializable {
    private Integer id;

    private String name;
    private Integer totalScore;
    private String description;
    private Integer flag;
    private Integer createBy;
    private Integer updateBy;

    @JSONField(serialize = false)
    private Date createtime;
    @JSONField(serialize = false)
    private Date updatetime;

    /***
     * 题目id
     */
    private List<Integer> topicsId;
}
