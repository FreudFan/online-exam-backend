package edu.sandau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/***
 * 错题分析
 */
@Data
public class WorryTopicAnalysis {
    //错误选项
    private String label;
    //错误次数
    private Integer value;

}
