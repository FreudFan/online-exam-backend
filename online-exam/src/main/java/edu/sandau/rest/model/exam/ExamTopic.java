package edu.sandau.rest.model.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExamTopic {
    @ApiModelProperty(value = "考试记录id")
    private Integer recordId;
    @ApiModelProperty(value = "题目id")
    private Integer topicId;
    @ApiModelProperty(value = "答案")
    private String answer;
    @ApiModelProperty(value = "用户做过的所有题目")
    List<ExamTopic> topics;
}
