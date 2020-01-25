package edu.sandau.service;

import edu.sandau.entity.Organization;

import java.util.List;

public interface OrganizationService {

    Organization save(Organization organization) throws Exception ;

    List<Organization> getALLSchool() throws Exception ;

    Organization update(Organization organization) throws Exception;

    /***
     * 根据上级id查询子项
     * @return
     */
    List<Organization> getOrgByUpperId(Integer upperId) throws Exception ;

    /***
     * 根据学校查询学院
     * @param school_id
     * @return
     */
    List<Organization> getCollegesBySchool(Integer school_id) throws Exception ;

    /***
     * 根据学院查询专业
     * @param college_id
     * @return
     */
    List<Organization> getMajorByCollege(Integer college_id) throws Exception ;

}
