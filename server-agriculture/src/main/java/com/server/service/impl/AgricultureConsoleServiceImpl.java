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
    private  AgricultureConsoleMapper agricultureConsoleMapper;

    @Autowired
    private AgricultureTraceabilityLogMapper agricultureTraceabilityLogMapper;

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
    /**
     * 获取溯源统计数据，包括年、月、周的明细及增长率。
     *
     * @return 溯源统计数据Map，包含年、月、周明细及增长率
     */
    public Map<String, Object> listTraceTotal() {
        Map<String, Object> result = new HashMap<>();
        // 获取年、月、周的溯源明细数据
        result.put("year", agricultureConsoleMapper.getTraceTotalByYear());
        result.put("month", agricultureConsoleMapper.getTraceTotalByMonth());
        result.put("week", agricultureConsoleMapper.getTraceTotalByWeek());

        // 统计年增长率：对比本年与去年溯源数量
        Long thisYear = agricultureConsoleMapper.getTraceCountThisYear();  //本年
        Long lastYear = agricultureConsoleMapper.getTraceCountLastYear();  //去年
        // 将其* 1.0转换为double类型 保证后续除法是浮点运算，避免整型除法丢失小数
        // 使用 Math.round() 将结果四舍五入为最接近的整数，并存储为 long 类型
        long yearGrowth = (lastYear != null && lastYear > 0) ? Math.round(((thisYear - lastYear) * 1.0 / lastYear) * 100) : 0L;
        Map<String, Object> yearGrowthMap = new HashMap<>();
        yearGrowthMap.put("thisYear", thisYear);
        yearGrowthMap.put("lastYear", lastYear);
        yearGrowthMap.put("yearGrowth", yearGrowth); // 结果为整数
        result.put("yearGrowth", yearGrowthMap);

        // 统计月增长率：对比本月与上月溯源数量
        Long thisMonth = agricultureConsoleMapper.getTraceCountThisMonth();  //本月
        Long lastMonth = agricultureConsoleMapper.getTraceCountLastMonth();  //上个月
        long monthGrowth = (lastMonth != null && lastMonth > 0) ? Math.round(((thisMonth - lastMonth) * 1.0 / lastMonth) * 100) : 0L;
        Map<String, Object> monthGrowthMap = new HashMap<>();
        monthGrowthMap.put("thisMonth", thisMonth);
        monthGrowthMap.put("lastMonth", lastMonth);
        monthGrowthMap.put("monthGrowth", monthGrowth); // 结果为整数
        result.put("monthGrowth", monthGrowthMap);

        // 统计周增长率：对比本周与上周溯源数量
        Long thisWeek = agricultureConsoleMapper.getTraceCountThisWeek();  //本周
        Long lastWeek = agricultureConsoleMapper.getTraceCountLastWeek();  //上周
        long weekGrowth = (lastWeek != null && lastWeek > 0) ? Math.round(((thisWeek - lastWeek) * 1.0 / lastWeek) * 100) : 0L;
        Map<String, Object> weekGrowthMap = new HashMap<>();
        weekGrowthMap.put("thisWeek", thisWeek);
        weekGrowthMap.put("lastWeek", lastWeek);
        weekGrowthMap.put("weekGrowth", weekGrowth); // 结果为整数
        result.put("weekGrowth", weekGrowthMap);

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
        return String.format("+%.0f%%", change);
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
