package model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Topics implements Serializable {
    private Integer topics_id;
    private Integer type;
    private Integer difficult;
    private String description;
    private String correctkey;
    private Integer topicsmark;
    private Integer subject_id;
}
