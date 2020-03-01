package edu.sandau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * 院校实体
 */
@Data
public class Organization implements Serializable {
    private Integer id;
    /***
     * 名称
     */
    private String name;
    /***
     * 类型枚举 0:学校 1:学院 2:专业 3:班级
     */
    private Integer type;
    /***
     * 上一级 id
     */
    private Integer upper_id = 0;

    @JsonIgnore
    private Date createtime;
    @JsonIgnore
    private Date updatetime;
}
