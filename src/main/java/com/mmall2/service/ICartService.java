package com.mmall2.service;

import com.mmall2.common.ServerResponse;
import com.mmall2.vo.CartVo;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/2/21
 * @Content:
 */
public interface ICartService {
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
}
