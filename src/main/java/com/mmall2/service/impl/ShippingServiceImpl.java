package com.mmall2.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall2.common.ServerResponse;
import com.mmall2.dao.ShippingMapper;
import com.mmall2.pojo.Shipping;
import com.mmall2.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/2/24
 * @Content:
 */

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0){
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());// 为何能shipping.getId()，请看shippingMapper.insert里的注释
            return ServerResponse.createBySuccess("新建地址成功", result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    public ServerResponse<String> del(Integer userId, Integer shippingId){
//TODO    int resultCount = shippingMapper.deleteByPrimaryKey(shippingId);这个具有横向越权的可能，因为没有关联其对应的用户，
//        这样就有删除别人地址的风险
        int result = shippingMapper.deleteByShippingIdUserId(userId, shippingId);
        if(result > 0){
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createBySuccess("删除地址失败");
    }

    public ServerResponse<String> update  (Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        Shipping curShipping = shippingMapper.selectByUserIdShippingId(userId, shipping.getId());
        shipping.setCreateTime(curShipping.getCreateTime());// 获取它创建的时间并更新到新的Shipping里
        int rowCount = shippingMapper.updateByShipping(shipping);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    public ServerResponse<Shipping> select  (Integer userId, Integer shippingId){
        Shipping shipping = shippingMapper.selectByUserIdShippingId(userId, shippingId);
        if(shipping == null ){
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess("查询地址成功", shipping);
    }

    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectListByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);

    }
}
