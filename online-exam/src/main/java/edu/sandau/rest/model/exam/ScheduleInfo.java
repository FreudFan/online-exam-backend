package edu.sandau.rest.model.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/***
 * 日程信息
 */
@Data
public class ScheduleInfo {

    @ApiModelProperty(value = "日程名称")
    private String name;

    @ApiModelProperty(value = "日程id")
    private Integer scheduleId;

    @ApiModelProperty(value = "日程类型名称")
    private String typeName;

    @ApiModelProperty(value = "日程类型id")
    private Integer type;

    @ApiModelProperty(value = "日程介绍")
    private String description;

    @ApiModelProperty(value = "考试成绩")
    private Double score;

    @ApiModelProperty(value = "考试开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "考试结束时间")
    private Date endTime;

}
