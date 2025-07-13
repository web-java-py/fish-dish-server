package com.server.domain.dto;

import lombok.Data;

/**
 * @Author: zbb
 * @Date: 2025/7/2 20:31
 */
@Data
public class AgriculturePastureDTO {
    private Long id; //ID
    private Long pasture_id;  //agriculture_crop_batch表的大棚id
    private String area;   //大棚面积
    private String remaining_area;   //大棚剩余面积
    private Long crop_area;   //agriculture_crop_batch表的种植面积
    private Long fish_area;
    private Long big_breeding_quantity; //最大分区数量
    private Long breeding_quantity;  //当前分区数量
}
