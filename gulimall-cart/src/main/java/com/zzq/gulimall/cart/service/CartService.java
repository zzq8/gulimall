package com.zzq.gulimall.cart.service;

import com.zzq.gulimall.cart.vo.CartItemVo;
import com.zzq.gulimall.cart.vo.CartVo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CartService {

    void clearCartInfo(String cartKey);

    /**
     * 将商品添加至购物车
     */
    CartItemVo addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItemVo getCartItem(Long skuId);

    CartVo getCart();

    void checkItem(Long skuId, Integer checked);

    void changeItemCount(Long skuId, Integer num);

    void deleteIdCartInfo(Integer skuId);

    List<CartItemVo> getUserCartItems() throws Exception;
}
