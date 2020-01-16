package edu.sandau.service;

import edu.sandau.entity.SysEnum;
import edu.sandau.rest.model.Page;

import java.util.List;
import java.util.Map;

public interface SysEnumService {
    /***
     * 添加枚举
     * @param sysEnum
     * @return
     * @throws Exception
     */
    SysEnum addEnum(SysEnum sysEnum);

    /***
     * 根据模块和类型拿到所有枚举
     * @param catalog
     * @param type
     * @return
     */
    List<Map<String, Object>> getEnumMap(String catalog, String type);

    /***
     * 根据模块和类型拿到所有枚举
     * @param catalog
     * @param type
     * @return
     */
    List<SysEnum> getEnums(String catalog, String type);

    /***
     * 通过 模块，类型，整型值 获得枚举
     * @param catalog
     * @param type
     * @param value
     * @return
     */
    SysEnum getEnum(String catalog, String type, Integer value);

    /***
     * 通过 模块，类型，枚举名 获得整型值
     * @param catalog
     * @param type
     * @param name
     * @return
     */
    Integer getEnumValue(String catalog, String type, String name);

    /***
     * 获取枚举名
     * @param catalog
     * @param type
     * @param value
     * @return
     */
    String getEnumName(String catalog, String type, Integer value);

    /***
     * 修改枚举值
     * @param sysEnum
     * @return
     * @throws Exception
     */
    SysEnum changeEnum(SysEnum sysEnum);

    /***
     * 删除枚举
     * @param sysEnum
     * @return
     * @throws Exception
     */
    Integer deleteEnum(SysEnum sysEnum);

    /***
     * 通过id删除枚举
     * @param id
     * @return
     * @throws Exception
     */
    Integer deleteEnumById(Integer id);

    /***
     * 分页查询所有枚举
     * @param page
     * @return
     */
    Page getEnumsByPage(Page page);
}
