package edu.sandau.service.impl;

import edu.sandau.dao.SubjectDao;
import edu.sandau.entity.Subject;
import edu.sandau.rest.model.Page;
import edu.sandau.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectDao subjectDao;

    @Override
    public Subject save(Subject subject) throws Exception {
        return subjectDao.save(subject);
    }

//    @Override
//    public Subject update(Subject subject) throws Exception {
//        int id = subject.getId();
//        subjectDao.update(subject);
//        return subjectDao.getSubjectById(id);
//    }


    @Override
    public Subject getSubjectById(Integer id) throws Exception {
        return subjectDao.getSubjectById(id);
    }

    @Override
    public Integer deleteById(Integer id) throws Exception {
        return subjectDao.delete(id);
    }

    @Override
    public Page showSub(Page page) {
        List<Subject> subjectList = subjectDao.showSub(page);
        page.setRows(subjectList);
        return page;
    }
}
