package edu.sandau.rest.model;

import edu.sandau.entity.Option;
import lombok.Data;

import java.util.Date;
import java.util.List;

/***
 * 返回给前端的错题实体类
 */
@Data
public class WorryTopicData {
    private Integer id;
    private String description;
    private String correctkey;
    private String worryanswer;
    private Integer difficult;
    private String difficultName;
    private Double topicmark;
    private String analysis;
    private Integer subject_id;
    private String createtime;

    /***
     * 选项
     */
    private List<Option> optionList;
}
