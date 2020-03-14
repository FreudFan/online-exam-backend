package edu.sandau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/***
 * 考试记录
 */
@Data
@NoArgsConstructor
public class ExamRecord {
    private Integer id;
    /***
     * 用户id
     */
    private Integer userId;
    /***
     * 试卷id
     */
    private Integer examId;
    /***
     * 考试成绩
     */
    private Double score;
    /***
     * 考试开始时间
     */
    private Date beginTime;
    /***
     * 考试结束时间
     */
    private Date endTime;

    @JsonIgnore
    private Date createtime;
    @JsonIgnore
    private Date updatetime;

    public ExamRecord(Integer userId, Integer examId, Date beginTime) {
        this.userId = userId;
        this.examId = examId;
        this.beginTime = beginTime;
    }
}
