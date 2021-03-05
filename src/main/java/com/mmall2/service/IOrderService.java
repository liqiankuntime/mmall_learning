package com.mmall2.service;

import com.mmall2.common.ServerResponse;

import java.util.Map;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/2/26
 * @Content:
 */
public interface IOrderService {
    ServerResponse pay(Long orderNo, Integer userId, String path);
    ServerResponse aliCallback(Map<String, String> params);
    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    ServerResponse<Object> createOrder(Integer userId, Integer shippingId);
}
