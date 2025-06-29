package com.server.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
