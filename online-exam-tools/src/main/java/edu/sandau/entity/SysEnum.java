package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class SysEnum {
    @JSONField(serialize = false)
    private Integer id;
    /***
     * 模块名
     */
    private String catalog;
    /***
     * 枚举类型
     */
    private String type;
    /***
     * 枚举名称
     */
    private String name;
    /***
     * 整型值
     */
    private Integer value;
    /***
     * 说明
     */
    private String description;

    @JSONField(serialize = false)
    private Date createtime;
    @JSONField(serialize = false)
    private Date updatetime;
}
