package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.server.domain.AgricultureClass;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureDeviceType;
import com.server.domain.AgriculturePasture;
import com.server.mapper.*;
import com.server.service.ScaleService;
import com.server.domain.vo.ScaleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScaleServiceImpl implements ScaleService {

    @Autowired
    private AgricultureDeviceMapper agricultureDeviceMapper;

    @Autowired
    private AgricultureDeviceTypeMapper agricultureDeviceTypeMapper;

    @Autowired
    private AgriculturePastureMapper agriculturePastureMapper;

    @Autowired
    private AgricultureCropBatchMapper agricultureCropBatchMapper;

    @Autowired
    private AgricultureClassMapper agricultureClassMapper;

    @Autowired
    private AgricultureMaterialMapper agricultureMaterialMapper;

    @Autowired
    private AgricultureMachineMapper agricultureMachineMapper;

    @Autowired
    private AgricultureBatchTaskMapper agricultureBatchTaskMapper;

    /**
     * 获取设备规模
     *
     * @return
     */
    @Override
    public List<ScaleVO> listDevice() {
        List<AgricultureDeviceType> agricultureDeviceTypes = agricultureDeviceTypeMapper.selectList(null);
        List<ScaleVO> result = new ArrayList<ScaleVO>();
        agricultureDeviceTypes.forEach(item -> {
            LambdaQueryWrapper<AgricultureDevice> lambda = new QueryWrapper<AgricultureDevice>().lambda();
            Integer count = agricultureDeviceMapper.selectCount(
                    lambda.eq(AgricultureDevice::getDeviceTypeId, item.getId())
            );
            result.add(
                    ScaleVO.builder()
                            .label(item.getTypeName())
                            .value(count)
                            .build()
            );
        });
        return result;
    }

    @Override
    public List<ScaleVO> listAgriculture() {
        List<AgriculturePasture> agriculturePastures = agriculturePastureMapper.selectList(null);
        int areaSum = agriculturePastures.stream().mapToInt(area -> Integer.parseInt(area.getArea())).sum();
        Integer cropBatchCount = agricultureCropBatchMapper.selectCount(null);
        Integer classCount = agricultureClassMapper.selectCount(null);
        Integer fishCount = agricultureClassMapper.selectCount(new QueryWrapper<AgricultureClass>().lambda().eq(AgricultureClass::getClassType,0));
        Integer dishCount = agricultureClassMapper.selectCount(new QueryWrapper<AgricultureClass>().lambda().eq(AgricultureClass::getClassType,1));
        Integer materialCount = agricultureMaterialMapper.selectCount(null);
        Integer machineCount = agricultureMachineMapper.selectCount(null);
        Integer batchTaskCount = agricultureBatchTaskMapper.selectCount(null);


        List<ScaleVO> result = new ArrayList<ScaleVO>();

        result.add(scaleVO("面积", areaSum));
        result.add(scaleVO("大棚", agriculturePastures.size()));
        result.add(scaleVO("分区", cropBatchCount));
        result.add(scaleVO("种类", classCount));
        result.add(scaleVO("鱼", fishCount));
        result.add(scaleVO("菜", dishCount));
        result.add(scaleVO("农资", materialCount));
        result.add(scaleVO("农机", machineCount));
        result.add(scaleVO("任务", batchTaskCount));
        return result;
    }

    private ScaleVO scaleVO(String label, Integer value) {
        return ScaleVO.builder()
                .label(label)
                .value(value)
                .build();
    }
}
