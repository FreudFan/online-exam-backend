package edu.sandau.rest.model.exam;

import lombok.Data;

@Data
public class ExamClazz {
    /***
     * 题目类型
     */
    private Integer type;
    /***
     * 题目个数
     */
    private Integer num;
    /***
     * 题目总分
     */
    private Double score;
    /***
     * 题目难度
     */
    private Integer difficult;
}

