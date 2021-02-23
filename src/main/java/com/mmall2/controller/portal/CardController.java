package com.mmall2.controller.portal;

import com.mmall2.common.Const;
import com.mmall2.common.ResponseCode;
import com.mmall2.common.ServerResponse;
import com.mmall2.pojo.User;
import com.mmall2.service.ICartService;
import com.mmall2.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/2/21
 * @Content:
 */
@Controller
@RequestMapping("/cart/")
public class CardController {

    @Autowired
    private ICartService iCartService;

    /**
     * 购物车添加商品
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iCartService.add(user.getId(), productId, count);
    }

    /**
     * 购物车更新
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iCartService.update(user.getId(), productId, count);
    }

    /**
     * 购物车商品删除
     * @param session
     * @param productIds 删除的商品ID "id1,id2,id3"
     * @return
     */
    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpSession session, String productIds){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }
}
