package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
     * 日程id
     */
    private Integer scheduleId;
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

    @JSONField(serialize = false)
    private Date createtime;
    @JSONField(serialize = false)
    private Date updatetime;

    public ExamRecord(Integer userId, Integer scheduleId, Date beginTime) {
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.beginTime = beginTime;
    }
}
