package edu.sandau.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Topic implements Serializable {
    private final String TABLE_NAME = "topic";

    private Integer id;
    private Integer type;
    private Integer difficult;
    private String description;
    private String correctkey;
    private Integer topicmark;
    private Integer subject_id;
}
