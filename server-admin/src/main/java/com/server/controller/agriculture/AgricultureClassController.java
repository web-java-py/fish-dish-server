package com.server.controller.agriculture;

import com.server.annotation.Log;
import com.server.annotation.SeeRefreshData;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.core.page.TableDataInfo;
import com.server.domain.AgricultureClass;
import com.server.enums.BusinessType;
import com.server.service.AgricultureClassService;
import com.server.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/agriculture/class")
@Api(tags = "种类")
public class AgricultureClassController extends BaseController {

    @Autowired
    private AgricultureClassService agricultureClassService;

    /**
     * 查询种类数据
     * @param agricultureClass
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询种类数据")
    public TableDataInfo list(AgricultureClass agricultureClass){
        startPage();
        return getDataTable(agricultureClassService.selectAgricultureClassList(agricultureClass));
    }

    /**
     * 新增种类数据
     * @param agricultureClass
     * @return
     */
    @Log(title = "种类管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增种类数据")
    @SeeRefreshData
    public AjaxResult add(@RequestBody AgricultureClass agricultureClass){
        return toAjax(agricultureClassService.addAgricultureClass(agricultureClass));
    }

    /**
     * 修改种类数据
     * @param agricultureClass
     * @return
     */
    @Log(title = "种类管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改种类数据")
    public AjaxResult edit(@RequestBody AgricultureClass agricultureClass){
        return toAjax(agricultureClassService.editAgricultureClass(agricultureClass));
    }

    /**
     * 删除种类数据
     * @param classId
     * @return
     */
    @Log(title = "种类管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{classId}")
    @ApiOperation("删除种类数据")
    @SeeRefreshData
    public AjaxResult del(@PathVariable Long classId){
        return toAjax(agricultureClassService.delAgricultureClass(classId));
    }

    /**
     * 导出种类数据
     * @param response
     * @param agricultureClass
     */
    @Log(title = "种类管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出种类数据")
    public void export(HttpServletResponse response, AgricultureClass agricultureClass)
    {
        List<AgricultureClass> list = agricultureClassService.selectAgricultureClassList(agricultureClass);
        ExcelUtil<AgricultureClass> util = new ExcelUtil<AgricultureClass>(AgricultureClass.class);
        util.exportExcel(response, list, "种类数据");
    }

    /**
     * 种类智能报告
     * @param agricultureClass
     */
    @Log(title = "种类智能报告", businessType = BusinessType.AI)
    @PostMapping("/ai")
    @ApiOperation("种类智能报告")
    public AjaxResult ai(@RequestBody AgricultureClass agricultureClass)
    {
        agricultureClassService.aiAddAgricultureClassReport(agricultureClass);
        return success();
    }
}
