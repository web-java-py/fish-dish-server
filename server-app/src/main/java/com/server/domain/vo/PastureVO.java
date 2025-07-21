package com.server.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PastureVO {
    private Long pastureId;
    private String pastureName;
    private boolean online;
    private LocalDateTime refreshDate;
    private Integer taskTotal;
    private Integer webcamTotal;
    private Integer deviceTotal;
    private List<Map<String, Object>> bindingDevice;
}
