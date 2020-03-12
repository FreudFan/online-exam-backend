package edu.sandau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * 试题
 */
@Data
public class Topic implements Serializable {
    //题目id主键
    private Integer id;
    //导入的Excel文件id
    private Integer file_id;
    //题目类型
    @ApiModelProperty(value = "题目种类枚举{0:作业,1:考试}, 前端需赋值, 未赋值则默认为0")
    private Integer type;
    //题目难度
    @ApiModelProperty(value = "题目难度枚举{0:易,1:中,2:难}, 前端需赋值, 未赋值则默认为0")
    private Integer difficult;
    //题目描述
    private String description;
    //正确答案
    private String correctkey;
    //题目分值
    private Double topicmark;
    //题目解析
    private String analysis;
    //所属学科id
    private Integer subject_id;
    //选项集合
    private List<Option> optionsList;
    @JsonIgnore
    private Date createtime;
    @JsonIgnore
    private Date updatetime;
}
