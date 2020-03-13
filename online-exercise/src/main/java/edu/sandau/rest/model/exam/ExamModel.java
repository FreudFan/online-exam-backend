package edu.sandau.rest.model.exam;

import lombok.Data;

import java.util.List;

@Data
public class ExamModel {
    /***
     * 学科id
     */
    private Integer subjectId;

    private List<ExamClazz> clazz;
}

