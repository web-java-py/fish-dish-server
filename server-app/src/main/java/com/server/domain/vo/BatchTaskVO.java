package com.server.domain.vo;

import com.server.domain.AgricultureBatchTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchTaskVO {
    private Long taskId;
    private String batchName;
    private String pastureName;
    private AgricultureBatchTask agricultureBatchTask;
}
