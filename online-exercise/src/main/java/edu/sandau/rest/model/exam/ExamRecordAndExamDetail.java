package edu.sandau.rest.model.exam;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/***
 * 考试记录
 */
@Data
@NoArgsConstructor
/*** 只包含试卷的一些信息和试卷分数信息 */
public class ExamRecordAndExamDetail {
    /***
     * 记录表id
     */
    private Integer id;
    /***
     * 试卷id
     */
    private Integer examId;
    /***
     * 用户id
     */
    private Integer userId;
    /***
     * 试卷难度
     */
    private Integer difficult;
    /***
     * 此次做题成绩
     */
    private Double score;
    /***
     * 做题开始时间
     */
    private LocalDateTime beginTime;
    /***
     * 做题结束时间
     */
    private LocalDateTime endTime;
    /***
     * 试卷的信息
     * //试卷的名字
     */
    private String name;
    /***
     * 试卷总分
     */
    private Double totalScore;
    /***
     * 试卷描述
     */
    private String description;
}
