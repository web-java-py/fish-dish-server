package com.server.service;

import com.server.domain.vo.PastureVO;

import java.util.List;

public interface AppAgricultureService {

    /**
     * 获取大棚
     * @return
     */
    List<PastureVO> listPasture();
}
