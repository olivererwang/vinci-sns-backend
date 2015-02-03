package com.vinci.backend.web.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.vinci.backend.domain.user.model.UserModel;
import com.vinci.backend.domain.user.service.UserService;
import com.vinci.backend.web.util.BaseController;
import com.vinci.common.base.api.APIResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.vinci.backend.web.WebConstants.*;

/**
 * curl -H "Content-Type: application/json" -d '{"username":"xyz","password":"xyz"}' http://localhost:3000/api/login
 * 用户相关的接口
 * Created by tim@vinci on 15-2-3.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse<UserModel> createUser(@RequestBody UserModel user, HttpServletResponse response) {
        if (user == null) {
            return convertErrorCode(ERROR_USER_IS_NULL);
        }
        if (StringUtils.isEmpty(user.getNickName())) {
            return convertErrorCode(ERROR_USER_NICKNAME_IS_NULL);
        }
        return APIResponse.returnSuccess(userService.createUser(user.getNickName(), user.getUserSettings()));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse<UserModel> updateUser(@RequestBody UserModel.UserSettings settings, HttpServletResponse response) {
        checkLogin();
        if (settings == null) {
            return convertErrorCode(ERROR_USER_IS_NULL);
        }
        UserModel userModel = new UserModel();
        userModel.setId(queryUserInfo().getUserId());
        userModel.setNickName(queryUserInfo().getUser().getNickName());
        userModel.setUserSettings(settings);
        userService.updateUserSettings(userModel);
        return APIResponse.returnSuccess(userModel);
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    @ResponseBody
    public APIResponse<UserModel> getUserById(@RequestParam("userid") long userid) {
        if (userid <= 0) {
            return convertErrorCode(ERROR_USER_IS_NOT_EXIST);
        }
        UserModel user = userService.getUserByUserID(userid);
        if (user == null) {
            return convertErrorCode(ERROR_USER_IS_NOT_EXIST);
        }
        return APIResponse.returnSuccess(user);
    }

    @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
    @ResponseBody
    public APIResponse<Map<Long, UserModel>> getUserById(@RequestParam("userids") String userids) {
        if (StringUtils.isEmpty(userids)) {
            return convertErrorCode(ERROR_USER_ID_ARGUMENT_INVALID);
        }
        Iterator<String> iter = Splitter.on(',').split(userids).iterator();
        List<Long> list = Lists.newArrayList();
        while (iter.hasNext()) {
            try {
                list.add(Long.parseLong(iter.next()));
            } catch (NumberFormatException e) {
                //ignore
            }
        }
        if (list.size() > MAX_LENGTH_OF_GET_USER_BY_IDS) {
            return convertErrorCode(ERROR_USER_ID_ARGUMENT_IS_TOO_LONG);
        }
        return APIResponse.returnSuccess(userService.getUserByUserID(list));
    }

    @RequestMapping(value = "/bindDevice", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse<UserModel> bindDevice(@RequestParam("imei") String imei, @RequestParam("mac") String macAddr) {
        checkLogin();
        userService.bindDevice(queryUserInfo().getUserId(), imei, macAddr);
        return APIResponse.returnSuccess(userService.getUserByUserID(queryUserInfo().getUserId()));
    }


    @RequestMapping(value = "/unbindDevice", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse<UserModel> unbindDevice(@RequestParam("imei") String imei, @RequestParam("mac") String macAddr) {
        checkLogin();
        userService.unbindDevice(queryUserInfo().getUserId(), imei, macAddr);
        return APIResponse.returnSuccess(userService.getUserByUserID(queryUserInfo().getUserId()));
    }
}
