package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.server.domain.AgricultureBatchTask;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgriculturePasture;
import com.server.domain.vo.PastureVO;
import com.server.mapper.AgricultureBatchTaskMapper;
import com.server.mapper.AgricultureCropBatchMapper;
import com.server.mapper.AgricultureDeviceMapper;
import com.server.mapper.AgriculturePastureMapper;
import com.server.service.AppAgricultureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppAgricultureServiceImpl implements AppAgricultureService {

    @Autowired
    private AgriculturePastureMapper agriculturePastureMapper;

    @Autowired
    private AgricultureCropBatchMapper agricultureCropBatchMapper;

    @Autowired
    private AgricultureBatchTaskMapper agricultureBatchTaskMapper;

    @Autowired
    private AgricultureDeviceMapper agricultureDeviceMapper;

    /**
     * 获取大棚
     *
     * @return
     */
    @Override
    public List<PastureVO> listPasture() {
        List<PastureVO> result = new ArrayList<PastureVO>();
        List<AgriculturePasture> agriculturePastures = agriculturePastureMapper.selectList(null);
        agriculturePastures.forEach(item -> {
            LambdaQueryWrapper<AgricultureCropBatch> cropBatchLambda = new QueryWrapper<AgricultureCropBatch>().lambda();
            cropBatchLambda.eq(AgricultureCropBatch::getPastureId, item.getId());
            List<AgricultureCropBatch> agricultureCropBatches = agricultureCropBatchMapper.selectList(cropBatchLambda);
            Integer taskTotal = 0;
            if (agricultureCropBatches.size() > 0){
                Long[] batchIds = agricultureCropBatches.stream().map(AgricultureCropBatch::getBatchId).toArray(Long[]::new);
                LambdaQueryWrapper<AgricultureBatchTask> batchTaskLambda = new QueryWrapper<AgricultureBatchTask>().lambda();
                for (Long batchId : batchIds) {
                    batchTaskLambda.eq(AgricultureBatchTask::getBatchId, batchId).or();
                }
                taskTotal = agricultureBatchTaskMapper.selectCount(batchTaskLambda);
            }
            LambdaQueryWrapper<AgricultureDevice> deviceLambda = new QueryWrapper<AgricultureDevice>().lambda();
            deviceLambda.eq(AgricultureDevice::getPastureId, item.getId());
            List<AgricultureDevice> agricultureDevices = agricultureDeviceMapper.selectList(deviceLambda);
            List<Map<String, Object>> devices = new ArrayList<Map<String, Object>>();
            agricultureDevices.forEach(agricultureDevice -> {
                Map<String, Object> hasMap = new HashMap<String, Object>();
                hasMap.put("label",agricultureDevice.getDeviceName());
                hasMap.put("value",agricultureDevice.getId());
                devices.add(hasMap);
            });
            PastureVO pastureVO = new PastureVO();
            pastureVO.setPastureId(item.getId());
            pastureVO.setPastureName(item.getName());
            pastureVO.setOnline(true);
            pastureVO.setRefreshDate(item.getUpdateTime());
            pastureVO.setTaskTotal(taskTotal);
            pastureVO.setWebcamTotal(0);
            pastureVO.setDeviceTotal(agricultureDevices.size());
            pastureVO.setBindingDevice(devices);
            result.add(pastureVO);
        });
        return result;
    }
}
