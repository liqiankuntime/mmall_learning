package com.mmall2.controller.backend;

import com.mmall2.common.Const;
import com.mmall2.common.ResponseCode;
import com.mmall2.common.ServerResponse;
import com.mmall2.pojo.User;
import com.mmall2.service.ICategoryService;
import com.mmall2.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/1/29
 * @Content:
 */

@Controller
@RequestMapping("/manage/category")//todo 为什么这个后面没有 / 而 UserControler 后面就有呢？
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    // 增加分類
    @RequestMapping("add_category.do")  // 默认是get请求方法
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value="parentId", defaultValue = "0") int parentId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用戶未登錄，請登錄");
        }
        //校驗是不是管理員
        if(iUserService.checkAdminRole(user).isSuccess()){
            // 管理員
            // 接下來增加處理分類的邏輯

            return iCategoryService.addCategory(categoryName, parentId);
        }else {
            return ServerResponse.createByErrorMessage("無權限操作，需要管理員權限");
        }
    }


    @RequestMapping("set_categoryName.do")
    @ResponseBody
    // 更新分類名稱
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用戶未登錄，請登錄");
        }
        //校驗是不是管理員
        if(iUserService.checkAdminRole(user).isSuccess()){
            // 管理員
            // 接下來更新分類的名稱 服務

            return iCategoryService.updateCategoryName(categoryId, categoryName);
        }else {
            return ServerResponse.createByErrorMessage("無權限操作，需要管理員權限");
        }
    }

    // 获取指定分类信息
    @RequestMapping(value="get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(
            HttpSession session,
            @RequestParam(value="categoryId", defaultValue = "0") Integer categoryId
    ){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用戶未登錄，請登錄");
        }
        //校驗是不是管理員
        if(iUserService.checkAdminRole(user).isSuccess()){
            // 管理員
            // 查询子节点的category信息，不递归保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);

        }else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    // 获取当前category的Id，并递归查询它的子节点的Id
    @RequestMapping(value="get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildreenCategory(
            HttpSession session,
            @RequestParam(value="categoryId", defaultValue = "0") Integer categoryId
    ){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用戶未登錄，請登錄");
        }
        //校驗是不是管理員
        if(iUserService.checkAdminRole(user).isSuccess()){
            // 管理員
            // 查找当前节点的Id并递归它子节点的id
            return iCategoryService.selectCategoryAndChildrenById(categoryId);

        }else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

}
