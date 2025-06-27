package com.server.service.impl;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.server.core.page.TableDataInfo;
import com.server.domain.dto.IaPartitionFoodPageDTO;
import com.server.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.IaPartitionFoodMapper;
import com.server.domain.IaPartitionFood;
import com.server.service.IaPartitionFoodService;

/**
 * 分区食品 采摘Service业务层处理
 * 
 * @author server
 * @date 2025-06-24
 */
@Service
public class IaPartitionFoodServiceImpl extends ServiceImpl<IaPartitionFoodMapper,IaPartitionFood> implements IaPartitionFoodService
{
    @Autowired
    private IaPartitionFoodMapper iaPartitionFoodMapper;

    /**
     * 查询分区食品 采摘
     * 
     * @param id 分区食品 采摘主键
     * @return 分区食品 采摘
     */
    @Override
    public IaPartitionFood selectIaPartitionFoodById(String id)
    {
        return iaPartitionFoodMapper.selectById(id);
    }

    /**
     * 查询分区食品 采摘列表
     * 
     * @param iaPartitionFood 分区食品 采摘
     * @return 分区食品 采摘
     */
    @Override
    public List<IaPartitionFood> selectIaPartitionFoodList(IaPartitionFood iaPartitionFood)
    {
        LambdaQueryWrapper<IaPartitionFood> lambdaQueryWrapper = new QueryWrapper<IaPartitionFood>().lambda();
        return iaPartitionFoodMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增分区食品 采摘
     * 
     * @param iaPartitionFood 分区食品 采摘
     * @return 结果
     */
    @Override
    public int insertIaPartitionFood(IaPartitionFood iaPartitionFood)
    {
        return iaPartitionFoodMapper.insert(iaPartitionFood);
    }

    /**
     * 修改分区食品 采摘
     * 
     * @param iaPartitionFood 分区食品 采摘
     * @return 结果
     */
    @Override
    public int updateIaPartitionFood(IaPartitionFood iaPartitionFood)
    {
        return iaPartitionFoodMapper.updateById(iaPartitionFood);
    }

    /**
     * 批量删除分区食品 采摘
     * 
     * @param ids 需要删除的分区食品 采摘主键
     * @return 结果
     */
    @Override
    public int deleteIaPartitionFoodByIds(String[] ids)
    {
        return iaPartitionFoodMapper.deleteById(ids);
    }

    /**
     * 删除分区食品 采摘信息
     * 
     * @param id 分区食品 采摘主键
     * @return 结果
     */
    @Override
    public int deleteIaPartitionFoodById(String id)
    {
        return iaPartitionFoodMapper.deleteById(id);
    }

    @Override
    public List<IaPartitionFood> fy(IaPartitionFoodPageDTO baseDTO) {
        QueryWrapper wrapper = new QueryWrapper<Map<String, Object>>();
        wrapper.eq("ia_partition_id",baseDTO.getPartitionId());
        List<IaPartitionFood> iaPartitionFoods = iaPartitionFoodMapper.selectList(wrapper);
        //生成前端访问页面的条形二维码
        iaPartitionFoods.forEach(bean -> {
            try {
                // 直接生成二维码的Base64字符串
            String barcodeBase64 = QRCodeUtil.generateQRCode(bean.getId().toString());
                // 对应字段设置Base64字符串
                bean.setBarcode(barcodeBase64);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return iaPartitionFoods;
    }

    /**
     * 新增采摘 生成二维码展示
     */
//    @Override
//    public List<IaPartitionFood> XzList(IaPartitionFood iaPartitionFood) {
//        LambdaQueryWrapper<IaPartitionFood> lambdaQueryWrapper = new QueryWrapper<IaPartitionFood>().lambda();
//        lambdaQueryWrapper.eq(iaPartitionFood.getIaPartitionId() != null,
//                IaPartitionFood::getIaPartitionId,
//                iaPartitionFood.getIaPartitionId());
//        List<IaPartitionFood> iaPartitionFoods = iaPartitionFoodMapper.selectList(lambdaQueryWrapper);
//        //生成前端访问页面的条形二维码
//        iaPartitionFoods.forEach(bean -> {
//            try {
//                // 直接生成二维码的Base64字符串
//            String barcodeBase64 = QRCodeUtil.generateQRCode(bean.getId().toString());
//                // 对应字段设置Base64字符串
//                bean.setBarcode(barcodeBase64);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return iaPartitionFoods;
//    }

}
