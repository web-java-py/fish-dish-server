package com.server.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentifyDTO {
    private Object diagnosis;
    @JsonProperty("quick_check")
    private Object quickCheck;
    private Object suggestions;
}
