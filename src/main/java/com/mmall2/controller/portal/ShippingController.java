package com.mmall2.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall2.common.Const;
import com.mmall2.common.ResponseCode;
import com.mmall2.common.ServerResponse;
import com.mmall2.pojo.Shipping;
import com.mmall2.pojo.User;
import com.mmall2.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/2/24
 * @Content:
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {
    @Autowired
    private IShippingService iShippingService;


    /**
     * 1、新增收货地址
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session, Shipping shipping){
        // Shipping shipping  使用了springMvc的对象绑定方式，不然的话得一个一个字段的传递，就比较麻烦了
        // 一个一个的传类似这样传递：receiverName，receiverPhone，receiverMobile，receiverProvince，receiverCity
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iShippingService.add(user.getId(), shipping);
        // TODO 这里为啥不用ServerResponse.createBySuccess* 包裹了呢？
    }

    /**
     * 2、删除收货地址
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse del(HttpSession session, Integer shippingId){
        // Shipping shipping  使用了springMvc的对象绑定方式，不然的话得一个一个字段的传递，就比较麻烦了
        // 一个一个的传类似这样传递：receiverName，receiverPhone，receiverMobile，receiverProvince，receiverCity
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iShippingService.del(user.getId(), shippingId);
        // TODO 这里为啥不用ServerResponse.createBySuccess* 包裹了呢？
    }

    /**
     * 3、更新地址
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, Shipping shipping){
        // Shipping shipping  使用了springMvc的对象绑定方式，不然的话得一个一个字段的传递，就比较麻烦了
        // 一个一个的传类似这样传递：receiverName，receiverPhone，receiverMobile，receiverProvince，receiverCity
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iShippingService.update(user.getId(), shipping);
        // TODO 这里为啥不用ServerResponse.createBySuccess* 包裹了呢？
    }

    /**
     * 4、查看某个地址详情
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<Shipping> select(HttpSession session, Integer shippingId){
        // Shipping shipping  使用了springMvc的对象绑定方式，不然的话得一个一个字段的传递，就比较麻烦了
        // 一个一个的传类似这样传递：receiverName，receiverPhone，receiverMobile，receiverProvince，receiverCity
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iShippingService.select(user.getId(), shippingId);
    }

    /**
     * 地址列表分页
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpSession session
    ){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iShippingService.list(user.getId(), pageNum, pageSize);
    }

}
