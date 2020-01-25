package edu.sandau.entity;

import lombok.Data;

import java.io.Serializable;

/***
 * 试题选项
 */
@Data
public class Option implements Serializable {
    private Integer topic_id;
    private String name;
    private String content;

}
