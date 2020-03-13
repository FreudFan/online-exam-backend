package edu.sandau.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description = "用于接收Execl文件数据格式的题目")
@Data
public class TopicData {
    @ApiModelProperty(value = "文件id")
    private Integer id;
    @ApiModelProperty(value = "学科代码")
    private Integer subject_id;
    @ApiModelProperty(value = "学科种类")
    private Integer type;
    @ApiModelProperty(value = "题目数据")
    List<List<Object>> file;
}
