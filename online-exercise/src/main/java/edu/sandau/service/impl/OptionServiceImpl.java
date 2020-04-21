package edu.sandau.service.impl;

import edu.sandau.dao.OptionDao;
import edu.sandau.entity.Option;
import edu.sandau.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OptionServiceImpl implements OptionService {

    @Autowired
    private OptionDao optionDao;

    @Override
    public void insertOption(int keyId, List<Option> optionList) {
        optionDao.insertOption(keyId, optionList);
    }

    @Override
    public List<Option> findOptionById(int id) {
        return optionDao.findOptionById(id);
    }
}
