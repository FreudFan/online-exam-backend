package edu.sandau.rest.model;


import lombok.Data;

import java.util.List;

@Data
public class TopicData {
    int id;
    int subject_id;
    int type;
    List<List<Object>> file;
}
