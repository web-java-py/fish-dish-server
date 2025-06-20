package com.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @author 851543
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "agriculture_class_ai_report")
@ApiModel(value = "AgricultureClassAiReport", description = "种类智能报告表")
public class AgricultureClassAiReport extends BaseEntityPlus implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "report_id", type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "报告ID")
    private String reportId;

    @TableField(value = "class_id")
    @ApiModelProperty(value = "种类ID")
    private Long classId;

    @TableField(value = "optimalTemperature")
    @ApiModelProperty(value = "最适温度范围")
    private String optimalTemperature;

    @TableField(value = "optimalHumidity")
    @ApiModelProperty(value = "最适湿度范围")
    private String optimalHumidity;

    @TableField(value = "optimalLight")
    @ApiModelProperty(value = "最适光照范围")
    private String optimalLight;

    @TableField(value = "optimalSoilPh")
    @ApiModelProperty(value = "最适土壤pH范围")
    private String optimalSoilPh;

    @TableField(value = "optimalWindDirection")
    @ApiModelProperty(value = "最适风向")
    private String optimalWindDirection;

    @TableField(value = "optimalWindSpeed")
    @ApiModelProperty(value = "最适风速范围(m/s)")
    private String optimalWindSpeed;

    @TableField(value = "optimalWaterTemperature")
    @ApiModelProperty(value = "最适水温范围")
    private String optimalWaterTemperature;

    @TableField(value = "optimalWaterPh")
    @ApiModelProperty(value = "最适水质pH范围")
    private String optimalWaterPh;

    @TableField(value = "optimalDissolvedOxygen")
    @ApiModelProperty(value = "最适溶解氧范围(mg/L)")
    private String optimalDissolvedOxygen;

    @TableField(value = "optimalAmmonia")
    @ApiModelProperty(value = "最适氨氮范围(mg/L)")
    private String optimalAmmonia;

    @TableField(value = "optimalNitrite")
    @ApiModelProperty(value = "最适亚硝酸盐范围(mg/L)")
    private String optimalNitrite;

    @TableField(value = "growthRate")
    @ApiModelProperty(value = "生长速度(%)")
    private Integer growthRate;

    @TableField(value = "diseaseResistance")
    @ApiModelProperty(value = "抗病能力(%)")
    private Integer diseaseResistance;

    @TableField(value = "feedConversion")
    @ApiModelProperty(value = "饲料/肥料转化率(%)")
    private Integer feedConversion;

    @TableField(value = "marketAcceptance")
    @ApiModelProperty(value = "市场认可度(%)")
    private Integer marketAcceptance;

    @TableField(value = "waterManagement")
    @ApiModelProperty(value = "水质/水分管理建议")
    private String waterManagement;

    @TableField(value = "feedingManagement")
    @ApiModelProperty(value = "投喂/肥料管理建议")
    private String feedingManagement;

    @TableField(value = "diseasePrevention")
    @ApiModelProperty(value = "疾病防控/病虫害防治建议")
    private String diseasePrevention;

    @TableField(value = "environmentMonitoring")
    @ApiModelProperty(value = "环境监控/环境监控建议")
    private String environmentMonitoring;

    @TableField(value = "growthAssessment")
    @ApiModelProperty(value = "生长评估")
    private String growthAssessment;

    @TableField(value = "cultivationDifficulty")
    @ApiModelProperty(value = "养殖/种植难度")
    private String cultivationDifficulty;

    @TableField(value = "generalRecommendations")
    @ApiModelProperty(value = "综合建议")
    private String generalRecommendations;

    @TableField(value = "marketAnalysis")
    @ApiModelProperty(value = "市场分析")
    private String marketAnalysis;

}
