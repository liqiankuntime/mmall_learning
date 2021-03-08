package com.mmall2.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall2.common.Const;
import com.mmall2.common.ResponseCode;
import com.mmall2.common.ServerResponse;
import com.mmall2.pojo.User;
import com.mmall2.service.IOrderService;
import com.mmall2.service.IUserService;
import com.mmall2.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/3/7
 * @Content:
 */
@Controller
@RequestMapping("/manage/order")
public class OrderMangeController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IOrderService iOrderService;

    /**
     * 订单列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(
            HttpSession session,
            @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10")int pageSize

    ){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            // service
            return iOrderService.manageList(pageNum, pageSize);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 订单详情
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            // service
            return iOrderService.manageDetail(orderNo);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 按照订单号查找
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(
            HttpSession session,
            @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10")int pageSize,
            Long orderNo
    ){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            // service
            return iOrderService.manageSearch(orderNo, pageNum, pageSize);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 发货接口
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> ordersendGoods(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            // service
            return iOrderService.manageSendGoods(orderNo);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }






}























