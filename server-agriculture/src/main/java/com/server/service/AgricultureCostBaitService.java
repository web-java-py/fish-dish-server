package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureCostBait;
import com.server.domain.AgricultureCostBait;

/**
 * 饵料投喂Service接口
 * 
 * @author server
 * @date 2025-06-14
 */
public interface AgricultureCostBaitService extends IService<AgricultureCostBait>
{
    /**
     * 查询饵料投喂
     * 
     * @param costId 饵料投喂主键
     * @return 饵料投喂
     */
    public AgricultureCostBait selectFishCostBaitByCostId(Long costId);

    /**
     * 查询饵料投喂列表
     * 
     * @param agricultureCostBait 饵料投喂
     * @return 饵料投喂集合
     */
    public List<AgricultureCostBait> selectFishCostBaitList(AgricultureCostBait agricultureCostBait);

    /**
     * 新增饵料投喂
     * 
     * @param agricultureCostBait 饵料投喂
     * @return 结果
     */
    public int insertFishCostBait(AgricultureCostBait agricultureCostBait);

    /**
     * 修改饵料投喂
     * 
     * @param agricultureCostBait 饵料投喂
     * @return 结果
     */
    public int updateFishCostBait(AgricultureCostBait agricultureCostBait);

    /**
     * 批量删除饵料投喂
     * 
     * @param costId 需要删除的饵料投喂主键集合
     * @return 结果
     */
    public int deleteFishCostBaitByCostIds(Long costId);

    /**
     * 删除饵料投喂信息
     * 
     * @param costId 饵料投喂主键
     * @return 结果
     */
    public int deleteFishCostBaitByCostId(String costId);
}
