package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

@Data
public class UploadFile implements Serializable {
    @JSONField(serialize = false)
    private final String TABLE_NAME = "upload_file";

    private Integer id;
    private String fileName;
    private String filePath;
    private Integer user_id;

    @JSONField(serialize = false)
    private File file;

    @JSONField(serialize = false)
    private Date createtime;
    @JSONField(serialize = false)
    private Date updatetime;
}
