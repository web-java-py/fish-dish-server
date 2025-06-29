package com.server.service;

import com.server.domain.vo.AgricultureConsoleVO;

import java.util.List;

public interface AgricultureConsoleService {

    /**
     * 卡片数据
     *
     * @return
     */
    List<AgricultureConsoleVO> cardData();

    /**
     * 统计数据
     *
     * @return
     */
    List<AgricultureConsoleVO> countData();
}
