package com.mmall2.service;

import com.mmall2.common.ServerResponse;
import com.mmall2.pojo.Category;

import java.util.List;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/1/29
 * @Content:
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse selectCategoryAndChildrenById(Integer categoryId);
}
