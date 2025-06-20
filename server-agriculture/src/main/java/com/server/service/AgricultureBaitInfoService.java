package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureBaitInfo;

/**
 * 饵料信息Service接口
 * 
 * @author server
 * @date 2025-06-14
 */
public interface AgricultureBaitInfoService extends IService<AgricultureBaitInfo>
{
    /**
     * 查询饵料信息
     * 
     * @param baitId 饵料信息主键
     * @return 饵料信息
     */
    public AgricultureBaitInfo selectAgricultureBaitInfoByBaitId(String baitId);

    /**
     * 查询饵料信息列表
     * 
     * @param agricultureBaitInfo 饵料信息
     * @return 饵料信息集合
     */
    public List<AgricultureBaitInfo> selectAgricultureBaitInfoList(AgricultureBaitInfo agricultureBaitInfo);

    /**
     * 新增饵料信息
     * 
     * @param agricultureBaitInfo 饵料信息
     * @return 结果
     */
    public int insertAgricultureBaitInfo(AgricultureBaitInfo agricultureBaitInfo);

    /**
     * 修改饵料信息
     * 
     * @param agricultureBaitInfo 饵料信息
     * @return 结果
     */
    public int updateAgricultureBaitInfo(AgricultureBaitInfo agricultureBaitInfo);

    /**
     * 批量删除饵料信息
     * 
     * @param baitId 需要删除的饵料信息主键集合
     * @return 结果
     */
    public int deleteAgricultureBaitInfoByBaitIds(Long baitId);

}
