package edu.sandau.rest.model;

import edu.sandau.entity.Option;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/***
 * 返回给前端的题目Model
 */
@ApiModel(description = "题目")
@Data
public class TopicModel {
    @ApiModelProperty(value = "题目id")
    private Integer id;
    @ApiModelProperty(value = "文件id")
    private Integer file_id;
    @ApiModelProperty(value = "题目类型")
    private String typeName;
    @ApiModelProperty(value = "题目类型Id")
    private Integer type;
    @ApiModelProperty(value = "题目难度")
    private String difficultName;
    @ApiModelProperty(value = "题目难度(整型)")
    private Integer difficult;
    @ApiModelProperty(value = "题目描述")
    private String description;
    @ApiModelProperty(value = "题目正确答案")
    private String correctkey;
    @ApiModelProperty(value = "题目分值")
    private Double topicmark;
    @ApiModelProperty(value = "题目解析")
    private String analysis;
    @ApiModelProperty(value = "所属学科")
    private String subjectName;
    @ApiModelProperty(value = "所属学科id")
    private Integer subject_id;
    @ApiModelProperty(value = "选项内容")
    private List<Option> optionsList;
}
