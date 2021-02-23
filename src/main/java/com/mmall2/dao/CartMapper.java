package com.mmall2.dao;

import com.mmall2.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mmall_cart
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mmall_cart
     *
     * @mbggenerated
     */
    int insert(Cart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mmall_cart
     *
     * @mbggenerated
     */
    int insertSelective(Cart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mmall_cart
     *
     * @mbggenerated
     */
    Cart selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mmall_cart
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Cart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mmall_cart
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdProductIds(@Param("userId") Integer userId, @Param("productIdList") List<String> productIdlist);
}
























