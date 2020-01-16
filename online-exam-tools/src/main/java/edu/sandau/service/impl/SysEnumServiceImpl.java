package edu.sandau.service.impl;

import edu.sandau.dao.SysEnumDao;
import edu.sandau.entity.SysEnum;
import edu.sandau.service.SysEnumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class SysEnumServiceImpl implements SysEnumService {
    @Autowired
    private SysEnumDao enumDao;

    @Override
    public SysEnum addEnum(SysEnum sysEnum) {
        return enumDao.save(sysEnum);
    }

    @Override
    public List<Map<String, Object>> getEnumMap(String catalog, String type) {
        return enumDao.getEnumMap(catalog, type);
    }

    @Override
    public List<SysEnum> getEnums(String catalog, String type) {
        return enumDao.getEnums(catalog, type);
    }

    @Override
    public SysEnum getEnum(String catalog, String type, Integer value) {
        return enumDao.getEnum(catalog, type, value);
    }

    @Override
    public Integer getEnumValue(String catalog, String type, String name) {
        return enumDao.getEnumValue(catalog, type, name);
    }

    @Override
    public String getEnumName(String catalog, String type, Integer value) {
        return enumDao.getEnumName(catalog, type, value);
    }

    @Override
    public SysEnum changeEnum(SysEnum sysEnum) throws Exception {
        enumDao.updateEnum(sysEnum);
        return enumDao.getEnumById(sysEnum.getId());
    }

    @Override
    public Integer deleteEnum(SysEnum sysEnum) throws Exception {
        return enumDao.deleteEnum(sysEnum);
    }

    @Override
    public Integer deleteEnumById(Integer id) throws Exception {
        return enumDao.deleteEnumById(id);
    }
}
