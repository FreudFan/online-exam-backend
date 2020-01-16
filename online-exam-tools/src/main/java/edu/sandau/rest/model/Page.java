package edu.sandau.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@ApiModel(description = "分页查询参数")
@Getter
public class Page {
    @ApiModelProperty(value = "当前页码")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "页面大小, 默认是5")
    private Integer pageSize = 5;

    @ApiModelProperty(value = "查询总数")
    private Integer total;

    @ApiModelProperty(value = "查询参数")
    private Map<String, Object> option;

    @ApiModelProperty(value = "查询内容")
    private List<?> rows;

    public void setPageNo(Integer pageNo) {
        if (pageNo < 1) {
            this.pageNo = 1;
        } else {
            this.pageNo = pageNo;
        }
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
        }
    }

    public void setOption(Map<String, Object> option) {
        this.option = option;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
