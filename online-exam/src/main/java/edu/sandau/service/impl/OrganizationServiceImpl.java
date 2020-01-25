package edu.sandau.service.impl;

import edu.sandau.dao.OrganizationDao;
import edu.sandau.enums.OrganizationEnum;
import edu.sandau.entity.Organization;
import edu.sandau.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationDao organizationDao;

    @Override
    public Organization save(Organization organization) throws Exception {
        return organizationDao.save(organization);
    }

    @Override
    public List<Organization> getALLSchool() throws Exception {
        return organizationDao.getOrgByValueAndUpperId(OrganizationEnum.SCHOOL.getValue(), null);
    }

    @Override
    public Organization getOrgById(Integer id) throws Exception {
        return organizationDao.getOrganizationById(id);
    }

    @Override
    public Organization update(Organization organization) throws Exception {
        int id = organization.getId();
        organizationDao.update(organization);
        return organizationDao.getOrganizationById(id);
    }

    @Override
    public List<Organization> getOrgByUpperId(Integer upperId) throws Exception {
        return organizationDao.getOrgByUpperId(upperId);
    }

    @Override
    public List<Organization> getCollegesBySchool(Integer school_id) throws Exception {
        return organizationDao.getOrgByValueAndUpperId(OrganizationEnum.COLLEGE.getValue(), school_id);
    }

    @Override
    public List<Organization> getMajorByCollege(Integer college_id) throws Exception {
        return organizationDao.getOrgByValueAndUpperId(OrganizationEnum.MAJOR.getValue(), college_id);
    }
}
