package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.server.domain.*;
import com.server.domain.vo.AgricultureConsoleVO;
import com.server.mapper.*;
import com.server.service.AgricultureConsoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class AgricultureConsoleServiceImpl implements AgricultureConsoleService {

    @Autowired
    private AgriculturePastureMapper agriculturePastureMapper;

    @Autowired
    private AgricultureCropBatchMapper agricultureCropBatchMapper;

    @Autowired
    private AgricultureClassMapper agricultureClassMapper;

    @Autowired
    private AgricultureBatchTaskMapper agricultureBatchTaskMapper;

    @Autowired
    private  AgricultureTraceabilityLogMapper agricultureTraceabilityLogMapper;

    /**
     * 获取农场数据
     *
     * @return
     */
    @Override
    public List<AgricultureConsoleVO> listAgriculture() {
        LambdaQueryWrapper<AgriculturePasture> lambdaPasture = new QueryWrapper<AgriculturePasture>().lambda();
        LambdaQueryWrapper<AgricultureCropBatch> lambdaCropBatch = new QueryWrapper<AgricultureCropBatch>().lambda();
        LambdaQueryWrapper<AgricultureClass> lambdaClass = new QueryWrapper<AgricultureClass>().lambda();

        Integer lastPastureTotal = agriculturePastureMapper.selectCount(
                lambdaPasture.lt(AgriculturePasture::getCreateTime, lastWeekSunday())
        );
        Integer lastCropBatchTotal = agricultureCropBatchMapper.selectCount(
                lambdaCropBatch.lt(AgricultureCropBatch::getCreateTime, lastWeekSunday())
        );
        Integer lastClassTotal = agricultureClassMapper.selectCount(
                lambdaClass.lt(AgricultureClass::getCreateTime, lastWeekSunday())
        );
        List<AgriculturePasture> lastPastureList = agriculturePastureMapper.selectList(
                lambdaPasture.lt(AgriculturePasture::getCreateTime, lastWeekSunday())
                        .select(AgriculturePasture::getArea)
        );
        Integer lastPastureAreaTotal = lastPastureList.stream().mapToInt(area -> Integer.parseInt(area.getArea())).sum();

        Integer pastureTotal = agriculturePastureMapper.selectCount(null);
        Integer cropBatchTotal = agricultureCropBatchMapper.selectCount(null);
        Integer classTotal = agricultureClassMapper.selectCount(null);
        List<AgriculturePasture> pastureList = agriculturePastureMapper.selectList(null);
        Integer pastureAreaTotal = pastureList.stream().mapToInt(area -> Integer.parseInt(area.getArea())).sum();

        List<AgricultureConsoleVO> result = new ArrayList<AgricultureConsoleVO>();

        result.add(fillAgricultureConsoleVO("农场大棚", pastureTotal, lastPastureTotal, "&#xe632"));
        result.add(fillAgricultureConsoleVO("农场分区", cropBatchTotal, lastCropBatchTotal, "&#xe66b"));
        result.add(fillAgricultureConsoleVO("农场种类", classTotal, lastClassTotal, "&#xe60a"));
        result.add(fillAgricultureConsoleVO("农场面积", pastureAreaTotal, lastPastureAreaTotal, "&#xe6dc"));

        return result;
    }

    /**
     * 获取任务数据
     *
     * @return
     */
    @Override
    public List<AgricultureConsoleVO> listBatchTask() {
        String[] taskStatus = {
                "未分配",
                "已分配",
                "进行中",
                "已完成"
        };
        String[] taskIcon = {
                "&#xe7d9",
                "&#xe712",
                "&#xe77f",
                "&#xe70f"
        };
        String[] taskClass = {
                "bg-primary",
                "bg-warning",
                "bg-error",
                "bg-success"
        };


        List<AgricultureConsoleVO> result = new ArrayList<AgricultureConsoleVO>();


        for (int i = 0; i < taskStatus.length; i++) {
            LambdaQueryWrapper<AgricultureBatchTask> lambda = new QueryWrapper<AgricultureBatchTask>().lambda();
            Integer lastTotal = agricultureBatchTaskMapper.selectCount(
                    lambda.eq(AgricultureBatchTask::getStatus, i)
                            .lt(AgricultureBatchTask::getCreateTime, yesterday())
            );
            lambda = new QueryWrapper<AgricultureBatchTask>().lambda();
            Integer total = agricultureBatchTaskMapper.selectCount(
                    lambda.eq(AgricultureBatchTask::getStatus, i)
            );
            result.add(fillAgricultureConsoleVO(taskStatus[i], total, lastTotal, taskIcon[i], taskClass[i]));
        }

        return result;
    }

    @Override
    public Map<String, Object> listTraceTotal() {
        Map<String, Object> result = new HashMap<>();
        //添加键值对
        result.put("year", agricultureTraceabilityLogMapper.getTraceTotalByYear());
        result.put("month", agricultureTraceabilityLogMapper.getTraceTotalByMonth());
        result.put("week", agricultureTraceabilityLogMapper.getTraceTotalByWeek());
        return result;
    }

    /**
     * 溯源统计
     * @return
     */


    private LocalDateTime yesterday() {
        return LocalDateTime.now().minusDays(1)
                .with(LocalTime.MIDNIGHT);
    }

    private LocalDateTime lastWeekSunday() {
        return LocalDateTime.now().minusWeeks(1)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .with(LocalTime.MIDNIGHT);
    }

    private String formatPercentageChange(Integer newValue, Integer oldValue) {
        if (oldValue == null || newValue == null || oldValue == 0) {
            return "+0%";
        }
        double change = Math.abs((newValue.doubleValue() - oldValue.doubleValue()) / oldValue.doubleValue()) * 100;
        return String.format("+%.1f%%", change);
    }

    private AgricultureConsoleVO fillAgricultureConsoleVO(String name, Integer currentCount, Integer lastCount, String icon) {
        return AgricultureConsoleVO.builder()
                .label(name)
                .value(currentCount)
                .change(formatPercentageChange(currentCount, lastCount))
                .icon(icon)
                .build();
    }

    private AgricultureConsoleVO fillAgricultureConsoleVO(String name, Integer currentCount, Integer lastCount, String icon, String classType) {
        return AgricultureConsoleVO.builder()
                .label(name)
                .value(currentCount)
                .change(formatPercentageChange(currentCount, lastCount))
                .icon(icon)
                .classType(classType)
                .build();
    }

}
