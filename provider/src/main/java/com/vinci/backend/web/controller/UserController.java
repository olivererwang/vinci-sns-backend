package com.vinci.backend.web.controller;

import com.vinci.backend.domain.user.model.UserModel;
import com.vinci.backend.domain.user.service.UserService;
import com.vinci.backend.web.util.BaseController;
import com.vinci.common.base.api.APIResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import static com.vinci.backend.web.WebConstants.ERROR_USER_IS_NULL;
import static com.vinci.backend.web.WebConstants.ERROR_USER_NICKNAME_IS_NULL;

/**
 * curl -H "Content-Type: application/json" -d '{"username":"xyz","password":"xyz"}' http://localhost:3000/api/login
 * 用户相关的接口
 * Created by tim@vinci on 15-2-3.
 */
@Controller
@RequestMapping("/user/")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse<UserModel> saveOrder(@RequestBody UserModel user, HttpServletResponse response) {
        if (user == null) {
            return convertErrorCode(ERROR_USER_IS_NULL);
        }
        if (StringUtils.isEmpty(user.getNickName())) {
            return convertErrorCode(ERROR_USER_NICKNAME_IS_NULL);
        }
        return APIResponse.returnSuccess(userService.createUser(user.getNickName(), user.getUserSettings()));
    }
}
