package com.server.controller.agriculture;

import com.server.annotation.Log;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.core.page.TableDataInfo;
import com.server.domain.AgricultureClass;
import com.server.domain.AgricultureJob;
import com.server.enums.BusinessType;
import com.server.service.AgricultureJobService;
import com.server.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/agriculture/job")
@Api(tags = "作业")
public class AgricultureJobController extends BaseController {

    @Autowired
    private AgricultureJobService agricultureJobService;

    /**
     * 查询作业数据
     * @param agricultureJob
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询作业数据")
    public TableDataInfo list(AgricultureJob agricultureJob){
        startPage();
        return getDataTable(agricultureJobService.selectAgricultureJobList(agricultureJob));
    }

    /**
     * 新增作业数据
     * @param agricultureJob
     * @return
     */
    @Log(title = "作业管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增作业数据")
    public AjaxResult add(@RequestBody AgricultureJob agricultureJob){
        return toAjax(agricultureJobService.addAgricultureJob(agricultureJob));
    }

    /**
     * 修改作业数据
     * @param agricultureJob
     * @return
     */
    @Log(title = "作业管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改作业数据")
    public AjaxResult edit(@RequestBody AgricultureJob agricultureJob){
        return toAjax(agricultureJobService.editAgricultureJob(agricultureJob));
    }

    /**
     * 删除作业数据
     * @param jobId
     * @return
     */
    @Log(title = "作业管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobId}")
    @ApiOperation("删除作业数据")
    public AjaxResult del(@PathVariable Long jobId){
        return toAjax(agricultureJobService.delAgricultureJob(jobId));
    }

    /**
     * 导出作业数据
     * @param response
     * @param agricultureJob
     */
    @Log(title = "作业管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出作业数据")
    public void export(HttpServletResponse response, AgricultureJob agricultureJob)
    {
        List<AgricultureJob> list = agricultureJobService.selectAgricultureJobList(agricultureJob);
        ExcelUtil<AgricultureJob> util = new ExcelUtil<AgricultureJob>(AgricultureJob.class);
        util.exportExcel(response, list, "作业数据");
    }

    /**
     * ai作业数据
     * @param agricultureClass
     */
    @Log(title = "作业管理", businessType = BusinessType.AI)
    @PostMapping("/ai")
    @ApiOperation("ai作业数据")
    public AjaxResult ai(@RequestBody AgricultureClass agricultureClass)
    {
        agricultureJobService.aiAddAgricultureJob(agricultureClass);
        return success();
    }
}
