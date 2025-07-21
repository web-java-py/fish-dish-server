package com.server.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchVO {
    private Long batchId;
    private String batchName;
    private String stage;
    private String[] images;
    private Long germplasmId;
    private Long vegetableId;
    private Long days;
    private Long pastureId;
    private String pastureName;
}
