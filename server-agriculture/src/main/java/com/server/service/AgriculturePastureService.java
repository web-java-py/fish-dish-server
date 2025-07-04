package com.server.service;

import java.util.List;
import com.server.domain.AgriculturePasture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureDeviceSensorAlert;
import com.server.domain.AgriculturePasture;
import com.server.domain.dto.AgriculturePastureDTO;

/**
 * 大棚Service接口
 * 
 * @author server
 * @date 2025-05-27
 */
public interface AgriculturePastureService  extends IService<AgriculturePasture>
{
    /**
     * 查询大棚
     * 
     * @param id 大棚主键
     * @return 大棚
     */
    public AgriculturePasture selectAgriculturePastureById(Long id);

    /**
     * 查询大棚列表
     * 
     * @param agriculturePasture 大棚
     * @return 大棚集合
     */
    public List<AgriculturePasture> selectAgriculturePastureList(AgriculturePasture agriculturePasture);

    /**
     * 新增大棚
     * 
     * @param agriculturePasture 大棚
     * @return 结果
     */
    public int insertAgriculturePasture(AgriculturePasture agriculturePasture);

    /**
     * 修改大棚
     * 
     * @param agriculturePasture 大棚
     * @return 结果
     */
    public int updateAgriculturePasture(AgriculturePasture agriculturePasture);

    /**
     * 批量删除大棚
     * 
     * @param ids 需要删除的大棚主键集合
     * @return 结果
     */
    public int deleteAgriculturePastureByIds(Long[] ids);

    /**
     * 删除大棚信息
     * 
     * @param id 大棚主键
     * @return 结果
     */
    public int deleteAgriculturePastureById(Long id);

    /**
     * 获取大棚剩余面积
     */
    List<AgriculturePastureDTO> selectRemainingArea(AgriculturePastureDTO agriculturePastureDTO);
}
