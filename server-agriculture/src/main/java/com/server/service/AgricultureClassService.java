package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureClass;

import java.util.List;


public interface AgricultureClassService extends IService<AgricultureClass> {

    /**
     * 查询种类数据
     * @param agricultureClass
     * @return
     */
    List<AgricultureClass> selectAgricultureClassList(AgricultureClass agricultureClass);



    /**
     * 新增种类数据
     * @param agricultureClass
     * @return
     */
    int addAgricultureClass(AgricultureClass agricultureClass);

    /**
     * 修改种类数据
     * @param agricultureClass
     * @return
     */
    int editAgricultureClass(AgricultureClass agricultureClass);

    /**
     * 删除种类数据
     * @param classId
     * @return
     */
    int delAgricultureClass(Long classId);

    /**
     * 种类智能报告
     * @param agricultureClass
     */
    void aiAddAgricultureClassReport(AgricultureClass agricultureClass);
}
