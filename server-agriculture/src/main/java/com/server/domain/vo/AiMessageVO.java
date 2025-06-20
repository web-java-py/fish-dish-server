package com.server.domain.vo;

import com.server.enums.ClassType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiMessageVO {
    private Long id;
    private String prompt;
    private String createBy;
    private ClassType classType;
}
