package com.server.controller.agriculture;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.server.annotation.SeeRefreshData;
import com.server.domain.dto.AgriculturePartitionFoodPageDTO;
import com.server.domain.vo.TraceabilityDetailVO;
import com.server.enums.SeeMessageType;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.server.annotation.Log;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.enums.BusinessType;
import com.server.domain.AgriculturePartitionFood;
import com.server.service.AgriculturePartitionFoodService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 分区食品 采摘Controller
 * 
 * @author server
 * @date 2025-06-24
 */
@RestController
@RequestMapping("/agriculture/partitionFood")
public class AgriculturePartitionFoodController extends BaseController
{
    @Autowired
    private AgriculturePartitionFoodService agriculturePartitionFoodService;

    /**
     * 查询分区食品 采摘列表
     */
    @PreAuthorize("@ss.hasPermi('agriculturePartitionFood:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgriculturePartitionFood agriculturePartitionFood)
    {
        startPage();
        return getDataTable(agriculturePartitionFoodService.selectagriculturePartitionFoodList(agriculturePartitionFood));
    }

    /**
     * 根据溯源码查询溯源详情信息（分区、大棚、批次任务、环境数据等）
     */
    @ApiOperation("根据溯源码查询溯源详情信息")
    @PreAuthorize("@ss.hasPermi('agriculturePartitionFood:traceDetail')")
    @GetMapping("/traceDetail")
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    public AjaxResult getTraceabilityDetail(@RequestParam("traceId") String traceId,
                                       HttpServletRequest request) {
    // 自动获取客户端信息
    String queryIp = getClientIpAddress(request);
    String userAgent = request.getHeader("User-Agent");
    String queryType = "web"; // 可以根据需要判断是web、app还是scan
    
    // 调用Service方法获取溯源详情
    TraceabilityDetailVO vo = agriculturePartitionFoodService.getTraceabilityDetailById(traceId, queryIp, userAgent, queryType);
    return success(vo);
}
    /**
     * 导出分区食品 采摘列表
     */
    @PreAuthorize("@ss.hasPermi('agriculturePartitionFood:export')")
    @Log(title = "分区食品 采摘", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgriculturePartitionFood agriculturePartitionFood)
    {
        List<AgriculturePartitionFood> list = agriculturePartitionFoodService.selectagriculturePartitionFoodList(agriculturePartitionFood);
        ExcelUtil<AgriculturePartitionFood> util = new ExcelUtil<AgriculturePartitionFood>(AgriculturePartitionFood.class);
        util.exportExcel(response, list, "分区食品 采摘数据");
    }

    /**
     * 获取分区食品 采摘详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculturePartitionFood:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(agriculturePartitionFoodService.selectagriculturePartitionFoodById(id));
    }

    /**
     * 新增分区食品 采摘
     */
    @PreAuthorize("@ss.hasPermi('agriculturePartitionFood:add')")
    @Log(title = "分区食品 采摘", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgriculturePartitionFood agriculturePartitionFood)
    {
        return toAjax(agriculturePartitionFoodService.insertagriculturePartitionFood(agriculturePartitionFood));
    }

    /**
     * 修改分区食品 采摘
     */
    @PreAuthorize("@ss.hasPermi('agriculturePartitionFood:edit')")
    @Log(title = "分区食品 采摘", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgriculturePartitionFood agriculturePartitionFood)
    {
        return toAjax(agriculturePartitionFoodService.updateagriculturePartitionFood(agriculturePartitionFood));
    }

    /**
     * 删除分区食品 采摘
     */
    @PreAuthorize("@ss.hasPermi('agriculturePartitionFood:remove')")
    @Log(title = "分区食品 采摘", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(agriculturePartitionFoodService.deleteagriculturePartitionFoodByIds(ids));
    }

    /**
     * 新增采摘 生成二维码展示
     */
    @ApiOperation("分页列表")
    @GetMapping(value = "/page")
    public TableDataInfo page(AgriculturePartitionFoodPageDTO baseDTO) {
        startPage();
        List<AgriculturePartitionFood> list = agriculturePartitionFoodService.fy(baseDTO);
        return getDataTable(list);
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
