package com.mmall2.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall2.common.Const;
import com.mmall2.common.ResponseCode;
import com.mmall2.common.ServerResponse;
import com.mmall2.pojo.User;
import com.mmall2.service.IProductService;
import com.mmall2.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/2/5
 * @Content:
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> getDetail(Integer productId){
        return iProductService.getProductDetail(productId);
    }


    // 客户端list
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> getList(
            @RequestParam(value = "keyword", required = false)String keyword,
            @RequestParam(value = "categoryId", required = false)Integer categoryId,
            @RequestParam(value="pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "orderBy", defaultValue = "") String orderBy
    ){
        return iProductService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }

}
