package com.server.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgricultureConsoleVO {
    private String label;
    private Integer value;
    private String change;
    private String icon;
    @JsonProperty("class")
    private String classType;
    private char traceCode;
    @TableField(value="query_time")
    private LocalDateTime queryTime;
    private  String foodType;
}
