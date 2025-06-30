package com.server.controller.agriculture;

import com.server.annotation.Log;
import com.server.domain.AgricultureCostBait;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.enums.BusinessType;
import com.server.service.AgricultureCostBaitService;
import com.server.core.page.TableDataInfo;

/**
 * 饵料投喂Controller
 *
 * @author server
 * @date 2025-06-14
 */
@RestController
@RequestMapping("/agriculture/costBait")
public class AgricultureCostBaitController extends BaseController
{
    @Autowired
    private AgricultureCostBaitService agricultureCostBaitService;

    /**
     * 查询饵料投喂列表
     */
    @PreAuthorize("@ss.hasPermi('Agriculture:bait:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureCostBait agricultureCostBait)
    {
        return getDataTable(agricultureCostBaitService.selectFishCostBaitList(agricultureCostBait));
    }

    /**
     * 获取饵料投喂详细信息
     */
    @PreAuthorize("@ss.hasPermi('Agriculture:bait:query')")
    @GetMapping(value = "/{costId}")
    public AjaxResult getInfo(@PathVariable("costId") Long costId)
    {
        return success(agricultureCostBaitService.selectFishCostBaitByCostId(costId));
    }

    /**
     * 新增饵料投喂
     */
    @PreAuthorize("@ss.hasPermi('Agriculture:bait:add')")
    @Log(title = "饵料投喂", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureCostBait agricultureCostBait)
    {
        return toAjax(agricultureCostBaitService.insertFishCostBait(agricultureCostBait));
    }

    /**
     * 修改饵料投喂
     */
    @PreAuthorize("@ss.hasPermi('Agriculture:bait:edit')")
    @Log(title = "饵料投喂", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureCostBait agricultureCostBait)
    {
        return toAjax(agricultureCostBaitService.updateFishCostBait(agricultureCostBait));
    }

    /**
     * 删除饵料投喂
     */
    @PreAuthorize("@ss.hasPermi('Agriculture:bait:remove')")
    @Log(title = "饵料投喂", businessType = BusinessType.DELETE)
	@DeleteMapping("/{costId}")
    public AjaxResult remove(@PathVariable Long costId)
    {
        return toAjax(agricultureCostBaitService.deleteFishCostBaitByCostIds(costId));
    }
}
