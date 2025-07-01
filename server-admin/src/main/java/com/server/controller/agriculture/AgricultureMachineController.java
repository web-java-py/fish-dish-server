package com.server.controller.agriculture;

import com.server.annotation.Log;
import com.server.annotation.SeeRefreshData;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.core.page.TableDataInfo;
import com.server.domain.AgricultureMachine;
import com.server.enums.BusinessType;
import com.server.enums.SeeMessageType;
import com.server.service.AgricultureMachineService;
import com.server.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: zbb
 * @Date: 2025/5/26 16:23
 */
@RestController
@RequestMapping("/agriculture/machine")
@Api(tags = "农机信息")
public class AgricultureMachineController extends BaseController {

    @Autowired
    private AgricultureMachineService agricultureMachineService;

    /**
     * 查询农机信息666
     * @param agricultureMachine
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询农机信息")
    public TableDataInfo list(AgricultureMachine agricultureMachine){
        //分页处理
        startPage();
        return getDataTable(agricultureMachineService.selectagricultureMachineList(agricultureMachine));
    }

    /**
     * 删除农机信息
     */

    @Log(title = "农机管理",businessType = BusinessType.DELETE)
    @DeleteMapping("/{machineId}")
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    public AjaxResult delete(@PathVariable Long machineId){
        return toAjax(agricultureMachineService.deleteById(machineId));
    }

    /**
     *新增农机信息
     */
    @PostMapping
    @Log(title = "农机管理",businessType = BusinessType.INSERT)
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    public AjaxResult add(@RequestBody AgricultureMachine agricultureMachine){
        return toAjax(agricultureMachineService.addAgricultureMachine(agricultureMachine));
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    @ApiOperation("农机信息")
    @Log(title = "农机信息",businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response,AgricultureMachine agricultureMachine){
        List<AgricultureMachine> agricultureMachines = agricultureMachineService.selectagricultureMachineList(agricultureMachine);
        ExcelUtil<AgricultureMachine> util = new ExcelUtil<AgricultureMachine>(AgricultureMachine.class);
        util.exportExcel(response,agricultureMachines,"农机信息");
    }

    /**
     * 修改农机信息
     */
    @PutMapping
    @ApiOperation("修改农机信息")
    @Log(title = "农机信息",businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody AgricultureMachine agricultureMachine){
        return toAjax(agricultureMachineService.updateagricultureMaterial(agricultureMachine));
    }
}
