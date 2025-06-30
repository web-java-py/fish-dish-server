package com.server.service;

import com.server.domain.vo.ScaleVO;

import java.util.List;

public interface ScaleService {
    /**
     * 获取设备规模
     * @return
     */
    List<ScaleVO> listDevice();

    /**
     * 获取农场规模
     * @return
     */
    List<ScaleVO> listAgriculture();
}
