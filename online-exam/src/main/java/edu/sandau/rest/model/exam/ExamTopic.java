package edu.sandau.rest.model.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExamTopic {
    @ApiModelProperty(value = "session id")
    private Integer recordId;
    @ApiModelProperty(value = "题目id")
    private Integer topicId;
    @ApiModelProperty(value = "答案")
    private String answer;
}
