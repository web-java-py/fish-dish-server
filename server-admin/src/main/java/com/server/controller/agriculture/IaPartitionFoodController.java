package com.server.controller.agriculture;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.server.annotation.Log;
import com.server.domain.dto.IaPartitionFoodPageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import com.server.domain.IaPartitionFood;
import com.server.service.IaPartitionFoodService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 分区食品 采摘Controller
 *
 * @author server
 * @date 2025-06-24
 */
@RestController
@RequestMapping("/iaPartitionFood")
@Api(tags = "收获")
public class IaPartitionFoodController extends BaseController
{
    @Autowired
    private IaPartitionFoodService iaPartitionFoodService;

    /**
     * 查询分区食品 采摘列表
     */
    @PreAuthorize("@ss.hasPermi('iaPartitionFood:list')")
    @GetMapping("/list")
    public TableDataInfo list(IaPartitionFood iaPartitionFood)
    {
        startPage();
        List<IaPartitionFood> list = iaPartitionFoodService.selectIaPartitionFoodList(iaPartitionFood);
        return getDataTable(list);
    }

    /**
     * 导出分区食品 采摘列表
     */
    @PreAuthorize("@ss.hasPermi('iaPartitionFood:export')")
    @Log(title = "分区食品 采摘", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, IaPartitionFood iaPartitionFood)
    {
        List<IaPartitionFood> list = iaPartitionFoodService.selectIaPartitionFoodList(iaPartitionFood);
        ExcelUtil<IaPartitionFood> util = new ExcelUtil<IaPartitionFood>(IaPartitionFood.class);
        util.exportExcel(response, list, "分区食品 采摘数据");
    }

    /**
     * 获取分区食品 采摘详细信息
     */
    @PreAuthorize("@ss.hasPermi('iaPartitionFood:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(iaPartitionFoodService.selectIaPartitionFoodById(id));
    }

    /**
     * 新增分区食品 采摘
     */
    @PreAuthorize("@ss.hasPermi('iaPartitionFood:add')")
    @Log(title = "分区食品 采摘", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IaPartitionFood iaPartitionFood)
    {
        return toAjax(iaPartitionFoodService.insertIaPartitionFood(iaPartitionFood));
    }

    /**
     * 修改分区食品 采摘
     */
    @PreAuthorize("@ss.hasPermi('iaPartitionFood:edit')")
    @Log(title = "分区食品 采摘", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IaPartitionFood iaPartitionFood)
    {
        return toAjax(iaPartitionFoodService.updateIaPartitionFood(iaPartitionFood));
    }

    /**
     * 删除分区食品 采摘
     */
    @PreAuthorize("@ss.hasPermi('iaPartitionFood:remove')")
    @Log(title = "分区食品 采摘", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(iaPartitionFoodService.deleteIaPartitionFoodByIds(ids));
    }

    /**
     * 新增采摘 生成二维码展示
     */
    @ApiOperation("分页列表")
    @GetMapping(value = "/page")
    public TableDataInfo page(IaPartitionFoodPageDTO baseDTO) {
        startPage();
        List<IaPartitionFood> list = iaPartitionFoodService.fy(baseDTO);
        return getDataTable(list);
    }
}
