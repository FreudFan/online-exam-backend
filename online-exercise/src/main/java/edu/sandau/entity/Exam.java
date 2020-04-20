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
    //主键id
    private Integer id;
    //学科id
    private Integer subject_id;
    //学科中文名
    private String subjectName;
    //试卷的名称
    private String name;
    //试卷的总分
    private Integer totalScore = 100;
    //试卷的描述
    private String description;
    //试卷难度
    private Integer difficult;
    //难度中文
    private String difficultName;
    //试卷标识,标识试卷可不可用 1:可用 0:不可用
    private Integer flag = 1;
    //创建人
    private Integer createBy;
    //更新人
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
