package com.mmall2.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall2.common.Const;
import com.mmall2.common.ResponseCode;
import com.mmall2.common.ServerResponse;
import com.mmall2.dao.CartMapper;
import com.mmall2.dao.ProductMapper;
import com.mmall2.pojo.Cart;
import com.mmall2.pojo.Product;
import com.mmall2.service.ICartService;
import com.mmall2.util.BigDecimalUtil;
import com.mmall2.util.PropertiesUtil;
import com.mmall2.vo.CartProductVo;
import com.mmall2.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/2/21
 * @Content:
 */
@Service("iCartService") // 为Controller里的自动注入起个名字 iCartService
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc()
            );
        }

        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            // 说明这个产品还不在购物车里，要增加一个这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);

            cartMapper.insert(cartItem);
        } else {
            // 说明产品已经在购物车里了，那么更新数量
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);
    }

    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc()
            );
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if(cart != null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.list(userId);
    }

    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds) {
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productIdList)){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc()
            );
        }
        cartMapper.deleteByUserIdProductIds(userId, productIdList);
        return this.list(userId);
    }

    public ServerResponse<CartVo> list(Integer userId){
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    public ServerResponse<CartVo> selectOrUnselect(Integer userId,Integer productId,  Integer checked){
        cartMapper.checkedOrUncheckedProduct(userId,productId, checked);
        return this.list(userId);
    }

    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }


    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if(CollectionUtils.isNotEmpty(cartList)){
            for(Cart cartItem: cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product != null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    // 库存判断
                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()){
                        // 说明 商品的库存数 大于 购物车里的数量；可以正常购买
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else {
                        // 说明 商品的库存数 小于 购物车里的数量
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        // 把购物车中 这个商品的数量更新为 有效的库存数量
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);// 把 修改的字段给更新了
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    // 计算 购物车中当前产品总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(
                            product.getPrice().doubleValue(),
                            cartProductVo.getQuantity()
                    ));
                    cartProductVo.setProductChecked(cartItem.getChecked());

                }

                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    // 如果已经勾选， 增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(
                            cartTotalPrice.doubleValue(),
                            cartProductVo.getProductTotalPrice().doubleValue()
                    );
                }
                cartProductVoList.add(cartProductVo);

            }
        }
        // 填充购物车中的产品的VO
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId){
         if(userId == null){
             return false;
         }
         // 未勾选的数量是0 说明全部选中了
         return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }
}













