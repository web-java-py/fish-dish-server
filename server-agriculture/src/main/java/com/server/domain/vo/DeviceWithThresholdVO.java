package com.server.domain.vo;

import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureThresholdConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("设备及其阈值配置VO")
public class DeviceWithThresholdVO {
    @ApiModelProperty("设备信息")
    private AgricultureDevice device;

    @ApiModelProperty("该设备的阈值配置列表")
    private List<AgricultureThresholdConfig> thresholdConfigList;
} 