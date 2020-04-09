package edu.sandau.service;

import edu.sandau.entity.Subject;
import edu.sandau.rest.model.Page;

import java.util.List;

public interface SubjectService {

    Subject save(Subject subject) throws Exception;

//    Subject update(Subject subject) throws Exception;

    Subject getSubjectById(Integer id) throws Exception;

    Integer deleteById(Integer id) throws Exception;

    Page showSub(Page page);
}
