package edu.sandau.rest.model;

import lombok.Data;

import java.util.List;

@Data
public class TopicData {
    private Integer id;
    private Integer subject_id;
    private Integer type;
    List<List<Object>> file;
}
