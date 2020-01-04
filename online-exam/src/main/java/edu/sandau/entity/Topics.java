package edu.sandau.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Topics implements Serializable {
    private final String TABLE_NAME = "topics";

    private Integer id;
    private Integer type;
    private Integer difficult;
    private String description;
    private String correctkey;
    private Integer topicsmark;
    private Integer subject_id;
}
