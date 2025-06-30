package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgriculturePartitionFood;
import com.server.domain.dto.AgriculturePartitionFoodPageDTO;

/**
 * 分区食品 采摘Service接口
 * 
 * @author server
 * @date 2025-06-24
 */
public interface AgriculturePartitionFoodService extends IService<AgriculturePartitionFood>
{
    /**
     * 查询分区食品 采摘
     * 
     * @param id 分区食品 采摘主键
     * @return 分区食品 采摘
     */
    public AgriculturePartitionFood selectagriculturePartitionFoodById(String id);

    /**
     * 查询分区食品 采摘列表
     * 
     * @param agriculturePartitionFood 分区食品 采摘
     * @return 分区食品 采摘集合
     */
    public List<AgriculturePartitionFood> selectagriculturePartitionFoodList(AgriculturePartitionFood agriculturePartitionFood);

    /**
     * 新增分区食品 采摘
     * 
     * @param agriculturePartitionFood 分区食品 采摘
     * @return 结果
     */
    public int insertagriculturePartitionFood(AgriculturePartitionFood agriculturePartitionFood);

    /**
     * 修改分区食品 采摘
     * 
     * @param agriculturePartitionFood 分区食品 采摘
     * @return 结果
     */
    public int updateagriculturePartitionFood(AgriculturePartitionFood agriculturePartitionFood);

    /**
     * 批量删除分区食品 采摘
     * 
     * @param ids 需要删除的分区食品 采摘主键集合
     * @return 结果
     */
    public int deleteagriculturePartitionFoodByIds(String[] ids);

    /**
     * 删除分区食品 采摘信息
     * 
     * @param id 分区食品 采摘主键
     * @return 结果
     */
    public int deleteagriculturePartitionFoodById(String id);

    /**
     * 新增采摘 生成二维码展示
     */
    List<AgriculturePartitionFood> fy(AgriculturePartitionFoodPageDTO baseDTO);


}
