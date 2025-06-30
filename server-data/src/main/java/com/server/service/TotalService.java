package com.server.service;

import java.util.Map;

public interface TotalService {
    /**
     * 获取统计数据
     *
     * @return
     */
    Map<String, Object> listTotal();
}
