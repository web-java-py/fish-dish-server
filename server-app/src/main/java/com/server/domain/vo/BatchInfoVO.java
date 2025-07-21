package com.server.domain.vo;

import com.server.domain.AgricultureBatchTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchInfoVO {
    private Long batchId;
    private String way;
    private String[] className;
    private Double area;
    private BatchVO batchVO;
    private List<AgricultureBatchTask> agricultureBatchTaskList;
}
