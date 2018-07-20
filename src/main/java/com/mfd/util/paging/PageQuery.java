package com.mfd.util.paging;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.util.Map;

public class PageQuery {

    @Getter
    @Setter
    @Min(value = 1, message = "当前页码不合法")
    private int pageNo = 1;

    @Getter
    @Setter
    @Min(value = 1, message = "每页展示数量不合法")
    private int pageSize = 10;

    @Setter
    private int offset;

    public int getOffset() {
        return (pageNo - 1) * pageSize;
    }
    @Getter
    @Setter
    public Map<Object,Object> param;

    @Setter
    @Getter
    private Integer imgCateId;
}
