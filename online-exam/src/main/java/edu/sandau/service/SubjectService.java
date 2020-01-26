package edu.sandau.service;

import edu.sandau.entity.Subject;

import java.util.List;

public interface SubjectService {

    Subject save(Subject subject) throws Exception;

    Subject update(Subject subject) throws Exception;

    List<Subject> getSubjectsByOrgId(Integer orgId) throws Exception;

    Integer deleteById(Integer id) throws Exception;
}
