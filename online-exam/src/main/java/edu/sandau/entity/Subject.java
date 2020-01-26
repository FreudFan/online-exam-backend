package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/***
 * 科目
 */
@Data
public class Subject {
    private Integer id;

    private String name;
    private Integer orgId;

    @JSONField(serialize = false)
    private Date createtime;
    @JSONField(serialize = false)
    private Date updatetime;
}
