package edu.sandau.rest.model;

import edu.sandau.entity.Option;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/***
 * 返回给前端的错题实体类
 */
@Data
public class WorryTopicData {
    @ApiModelProperty(value = "题目id")
    private Integer id;
    @ApiModelProperty(value = "题目描述")
    private String description;
    @ApiModelProperty(value = "正确答案")
    private String correctkey;
    @ApiModelProperty(value = "错误答案")
    private String worryanswer;
    @ApiModelProperty(value = "难度")
    private Integer difficult;
    @ApiModelProperty(value = "难度中文")
    private String difficultName;
    @ApiModelProperty(value = "分值")
    private Double topicmark;
    @ApiModelProperty(value = "解析")
    private String analysis;
    @ApiModelProperty(value = "学科id")
    private Integer subject_id;
    @ApiModelProperty(value = "学科名")
    private String subjectName;
    @ApiModelProperty(value = "错误次数")
    private String worrycount;
    @ApiModelProperty(value = "错误时间")
    private String createtime;

    /***
     * 选项
     */
    @ApiModelProperty(value = "选项列表")
    private List<Option> optionList;
}
