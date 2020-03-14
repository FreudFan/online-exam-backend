package edu.sandau.rest.model.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.sandau.entity.Exam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/***
 * 考试记录
 */
@Data
@NoArgsConstructor
/***
 * 只包含试卷的一些信息和试卷分数信息
 */
public class ExamRecordAndExamDeatil {
    //记录表id
    private Integer id;
    /***
     * 用户id
     */
    private Integer userId;

    /***
     * 此次做题成绩
     */
    private Double score;
    /***
     * 做题开始时间
     */
    private Date beginTime;
    /***
     * 做题结束时间
     */
    private Date endTime;

    /***
     * 试卷的信息
     */
    //试卷的名字
    private String name;

    //试卷总分
    private Double totalScore;
    //试卷描述
    private String description;
    @JsonIgnore
    private Date createtime;
    @JsonIgnore
    private Date updatetime;


}
