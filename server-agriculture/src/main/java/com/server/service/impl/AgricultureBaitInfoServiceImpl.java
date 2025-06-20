package com.server.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureBaitInfoMapper;
import com.server.domain.AgricultureBaitInfo;
import com.server.service.AgricultureBaitInfoService;

/**
 * 饵料信息Service业务层处理
 * 
 * @author server
 * @date 2025-06-14
 */
@Service
public class AgricultureBaitInfoServiceImpl extends ServiceImpl<AgricultureBaitInfoMapper,AgricultureBaitInfo> implements AgricultureBaitInfoService
{
    @Autowired
    private AgricultureBaitInfoMapper agricultureBaitInfoMapper;

    /**
     * 查询饵料信息
     * 
     * @param baitId 饵料信息主键
     * @return 饵料信息
     */
    @Override
    public AgricultureBaitInfo selectAgricultureBaitInfoByBaitId(String baitId)
    {
        return agricultureBaitInfoMapper.selectById(baitId);
    }

    /**
     * 查询饵料信息列表
     * 
     * @param agricultureBaitInfo 饵料信息
     * @return 饵料信息
     */
    @Override
    public List<AgricultureBaitInfo> selectAgricultureBaitInfoList(AgricultureBaitInfo agricultureBaitInfo)
    {
        LambdaQueryWrapper<AgricultureBaitInfo> lambdaQueryWrapper = new QueryWrapper<AgricultureBaitInfo>().lambda();
        return agricultureBaitInfoMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增饵料信息
     * 
     * @param agricultureBaitInfo 饵料信息
     * @return 结果
     */
    @Override
    public int insertAgricultureBaitInfo(AgricultureBaitInfo agricultureBaitInfo)
    {
        return agricultureBaitInfoMapper.insert(agricultureBaitInfo);
    }

    /**
     * 修改饵料信息
     * 
     * @param agricultureBaitInfo 饵料信息
     * @return 结果
     */
    @Override
    public int updateAgricultureBaitInfo(AgricultureBaitInfo agricultureBaitInfo)
    {
        return agricultureBaitInfoMapper.updateById(agricultureBaitInfo);
    }

    /**
     * 批量删除饵料信息
     * 
     * @param baitId 需要删除的饵料信息主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureBaitInfoByBaitIds(Long baitId)
    {
        return agricultureBaitInfoMapper.deleteById(baitId);
    }

}
