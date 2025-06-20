package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.core.page.TableDataInfo;
import com.server.domain.IaPartitionFood;
import com.server.domain.dto.IaPartitionFoodPageDTO;

/**
 * 分区食品 采摘Service接口
 * 
 * @author server
 * @date 2025-06-24
 */
public interface IaPartitionFoodService extends IService<IaPartitionFood>
{
    /**
     * 查询分区食品 采摘
     * 
     * @param id 分区食品 采摘主键
     * @return 分区食品 采摘
     */
    public IaPartitionFood selectIaPartitionFoodById(String id);

    /**
     * 查询分区食品 采摘列表
     * 
     * @param iaPartitionFood 分区食品 采摘
     * @return 分区食品 采摘集合
     */
    public List<IaPartitionFood> selectIaPartitionFoodList(IaPartitionFood iaPartitionFood);

    /**
     * 新增分区食品 采摘
     * 
     * @param iaPartitionFood 分区食品 采摘
     * @return 结果
     */
    public int insertIaPartitionFood(IaPartitionFood iaPartitionFood);

    /**
     * 修改分区食品 采摘
     * 
     * @param iaPartitionFood 分区食品 采摘
     * @return 结果
     */
    public int updateIaPartitionFood(IaPartitionFood iaPartitionFood);

    /**
     * 批量删除分区食品 采摘
     * 
     * @param ids 需要删除的分区食品 采摘主键集合
     * @return 结果
     */
    public int deleteIaPartitionFoodByIds(String[] ids);

    /**
     * 删除分区食品 采摘信息
     * 
     * @param id 分区食品 采摘主键
     * @return 结果
     */
    public int deleteIaPartitionFoodById(String id);


    List<IaPartitionFood> fy(IaPartitionFoodPageDTO baseDTO);


    /**
     * 新增采摘 生成二维码展示
     */
//    List<IaPartitionFood> XzList(IaPartitionFood iaPartitionFood);



}
