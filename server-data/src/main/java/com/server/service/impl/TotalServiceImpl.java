package com.server.service.impl;

import com.server.service.ScaleService;
import com.server.service.TotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TotalServiceImpl implements TotalService {

    @Autowired
    private ScaleService scaleService;

    /**
     * 获取统计数据
     *
     * @return
     */
    @Override
    public Map<String, Object> listTotal() {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("device", scaleService.listDevice());
        hashMap.put("agriculture",  scaleService.listAgriculture());
        return hashMap;
    }
}
