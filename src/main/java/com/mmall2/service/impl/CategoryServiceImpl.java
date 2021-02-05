package com.mmall2.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall2.common.ServerResponse;
import com.mmall2.dao.CategoryMapper;
import com.mmall2.pojo.Category;
import com.mmall2.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/1/29
 * @Content: 要把service注入到controler中供controler使用；注入前先声明 @Service("iCategoryService")
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;
    public ServerResponse addCategory(String categoryName, Integer parentId){
        if(StringUtils.isBlank(categoryName) || parentId == null){
            return ServerResponse.createByErrorMessage("添加品類參數錯誤");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);// 默認這個分類是可用的

        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("添加品類成功");
        }
        return ServerResponse.createByErrorMessage("添加品類失敗");
    }

    public ServerResponse updateCategoryName(Integer categoryId, String categoryName){
        if(StringUtils.isBlank(categoryName) || categoryId == null){
            return ServerResponse.createByErrorMessage("更新品類參數錯誤");
        }
        Category category = new Category();
        category.setId(categoryId);// 通過Id 选择更新
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("更新品类名称成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名称失败");
    }

    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    // 递归查询本节点id以及子节点id
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildrenCategory(categorySet, categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for(Category categorySetItem : categorySet){
                categoryIdList.add(categorySetItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);

    }
    // 递归查找子节点
    private Set<Category> findChildrenCategory(Set<Category> categorySet, Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }
        // 查找子节点
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for(Category categoryItem: categoryList){
            findChildrenCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }

}


