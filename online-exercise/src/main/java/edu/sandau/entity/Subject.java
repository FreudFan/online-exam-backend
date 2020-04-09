package edu.sandau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/***
 * 科目
 */
@Data
public class Subject {
    private Integer id;

    private String name;

    @JsonIgnore
    private Date createtime;
    @JsonIgnore
    private Date updatetime;
}
