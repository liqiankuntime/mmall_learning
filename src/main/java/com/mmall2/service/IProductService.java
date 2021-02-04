package com.mmall2.service;

import com.github.pagehelper.PageInfo;
import com.mmall2.common.ServerResponse;
import com.mmall2.pojo.Product;
import com.mmall2.vo.ProductDetailVo;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/1/31
 * @Content:
 */
public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchproduct(String productName, Integer productId, int pageNum, int pageSize);
}
