package model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class Topics implements Serializable {
    @JSONField(serialize = false)
    private @Getter final String TABLE_NAME = "topics";

    private Integer topics_id;
    private Integer type;
    private Integer difficult;
    private String description;
    private String correctkey;
    private Integer topicsmark;
    private Integer subject_id;
}
