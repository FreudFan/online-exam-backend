package edu.sandau.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Topic implements Serializable {

    private Integer id;
    private Integer file_id;
    private Integer type;
    private Integer difficult;
    private String description;
    private String correctkey;
    private Double topicmark;
    private String analysis;
    private Integer subject_id;
    private List<Option> optionsList;
}
