package com.server.service;

import com.server.domain.vo.BatchInfoVO;
import com.server.domain.vo.BatchTaskVO;
import com.server.domain.vo.BatchVO;

import java.util.List;
import java.util.Map;

public interface AppConsoleService {

    /**
     * 获取分区
     *
     * @return
     */
    List<BatchVO> listBatch();

    /**
     * 获取分区详情
     * @param id
     * @return
     */
    BatchInfoVO batchInfo(Long id);

    /**
     * 获取大棚（选择框）
     *
     * @return
     */
    List<Map<String, Object>> listPastureNameMap();

    /**
     * 获取任务
     *
     * @return
     */
    List<BatchTaskVO> listBatchTask();
}
