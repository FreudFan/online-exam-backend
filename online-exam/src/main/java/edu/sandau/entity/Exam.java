package edu.sandau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer subject_id;
    private String name;
    private Integer totalScore = 100;
    private String description;
    private Integer flag;
    private Integer createBy;
    private Integer updateBy;

    @JsonIgnore
    private Date createtime;
    @JsonIgnore
    private Date updatetime;

    /***
     * 题目
     */
    private List<Topic> topics;
}
