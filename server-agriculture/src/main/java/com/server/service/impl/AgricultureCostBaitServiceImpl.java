package com.server.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureCostBait;
import com.server.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureCostBaitMapper;
import com.server.domain.AgricultureCostBait;
import com.server.service.AgricultureCostBaitService;

/**
 * 饵料投喂Service业务层处理
 * 
 * @author server
 * @date 2025-06-14
 */
@Service
public class AgricultureCostBaitServiceImpl extends ServiceImpl<AgricultureCostBaitMapper,AgricultureCostBait> implements AgricultureCostBaitService
{
    @Autowired
    private AgricultureCostBaitMapper fishCostBaitMapper;

    /**
     * 查询饵料投喂
     * 
     * @param costId 饵料投喂主键
     * @return 饵料投喂
     */
    @Override
    public AgricultureCostBait selectFishCostBaitByCostId(Long costId)
    {
        return fishCostBaitMapper.selectById(costId);
    }

    /**
     * 查询饵料投喂列表
     * 
     * @param agricultureCostBait 饵料投喂
     * @return 饵料投喂
     */
    @Override
    public List<AgricultureCostBait> selectFishCostBaitList(AgricultureCostBait agricultureCostBait)
    {
        LambdaQueryWrapper<AgricultureCostBait> lambdaQueryWrapper = new QueryWrapper<AgricultureCostBait>().lambda();
        if (agricultureCostBait.getTaskId() != null){
            lambdaQueryWrapper.eq(AgricultureCostBait::getTaskId,agricultureCostBait.getTaskId());
        }
        return fishCostBaitMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增饵料投喂
     * 
     * @param agricultureCostBait 饵料投喂
     * @return 结果
     */
    @Override
    public int insertFishCostBait(AgricultureCostBait agricultureCostBait)
    {

        return fishCostBaitMapper.insert(agricultureCostBait);
    }

    /**
     * 修改饵料投喂
     * 
     * @param agricultureCostBait 饵料投喂
     * @return 结果
     */
    @Override
    public int updateFishCostBait(AgricultureCostBait agricultureCostBait)
    {
        return fishCostBaitMapper.updateById(agricultureCostBait);
    }

    /**
     * 批量删除饵料投喂
     * 
     * @param costId 需要删除的饵料投喂主键
     * @return 结果
     */
    @Override
    public int deleteFishCostBaitByCostIds(Long costId)
    {
        return fishCostBaitMapper.deleteById(costId);
    }

    /**
     * 删除饵料投喂信息
     * 
     * @param costId 饵料投喂主键
     * @return 结果
     */
    @Override
    public int deleteFishCostBaitByCostId(String costId)
    {
        return fishCostBaitMapper.deleteById(costId);
    }
}
