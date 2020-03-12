package edu.sandau.service;

import edu.sandau.entity.Option;
import java.util.List;

public interface OptionService {


    /***
     * 将选项数据插入选项表
     * @param keyId
     * @param optionList
     */
     void insertOption(int keyId, List<Option> optionList);


    /***
     * 根据题目表id查找对应的选项
     * @param id
     * @return
     */
     List<Option> findOptionById(int id);
}
