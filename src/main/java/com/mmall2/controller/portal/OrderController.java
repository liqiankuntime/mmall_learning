package com.mmall2.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall2.common.Const;
import com.mmall2.common.ResponseCode;
import com.mmall2.common.ServerResponse;
import com.mmall2.dao.PayInfoMapper;
import com.mmall2.pojo.User;
import com.mmall2.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.krb5.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/2/26
 * @Content:
 */
@Controller
@RequestMapping("/order/")
public class OrderController {

    public static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;



    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        //注意： 这个path后面没有 "/"
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo, user.getId(), path);
    }

    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){

        Map params = Maps.newHashMap();
        Map requstParams = request.getParameterMap();// {key: [val1, val2, val3]}
        // 因为 requstParams 是map类型不能直接for循环，而是用遍历器循环
        for(Iterator iter = requstParams.keySet().iterator(); iter.hasNext(); ){
            String keyName = (String)iter.next();
            String[] values = (String[])requstParams.get(keyName);
            String valueStr = "";
            for(int i=0; i< values.length; i++){
                valueStr = (i == values.length)? valueStr+values[i]: valueStr + values[i] + ",";
            }
            params.put(keyName, valueStr);
        }
        logger.info(
                "支付宝回调, sign:{}, trade_status:{}, 参数:{}",
                params.get("sign"), params.get("trade_status"), params.toString()
        );

        // 重点
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(
                    params,
                    Configs.getAlipayPublicKey(),
                    "utf-8", Configs.getSignType() );
            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求，验证不通过，再请求就报警");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常", e);
        }
        // todo 验证各种数据


        ServerResponse result = iOrderService.aliCallback(params);
        if(result.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }


        return Const.AlipayCallback.RESPONSE_FAILED;
    }


    // 前台轮询查询订单支付状态的接口
    // 当付款完成时，在付款码页面调用这个接口，看是不是付款成功了，付款成功会给一个执言，然后跳转到订单页面
    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }

        ServerResponse result = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        if(result.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }
}
