package edu.sandau.entity;

import lombok.Data;

import java.io.Serializable;

/***
 * 试题选项
 */
@Data
public class Option implements Serializable {
    //与题目关联，题目id
    private Integer topic_id;
    //选项(A,B,C,D...)
    private String name;
    //选项具体内容
    private String content;

}
