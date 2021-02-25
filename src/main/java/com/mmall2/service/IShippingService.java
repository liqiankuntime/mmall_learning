package com.mmall2.service;

import com.github.pagehelper.PageInfo;
import com.mmall2.common.ServerResponse;
import com.mmall2.pojo.Shipping;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/2/24
 * @Content:
 */
public interface IShippingService {
    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse del(Integer userId, Integer shippingId);

    ServerResponse update  (Integer userId, Shipping shipping);

    ServerResponse<Shipping> select  (Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
