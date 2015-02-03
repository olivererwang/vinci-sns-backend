package com.vinci.backend.web.controller;

import com.google.common.collect.Lists;
import com.vinci.backend.domain.relations.model.Attention;
import com.vinci.backend.domain.relations.service.RelationService;
import com.vinci.backend.domain.user.model.UserModel;
import com.vinci.backend.domain.user.service.UserService;
import com.vinci.backend.web.util.BaseController;
import com.vinci.common.base.api.APIResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static com.vinci.backend.web.WebConstants.*;

/**
 * 关注关系的controller
 * Created by tim@vinci on 15-2-3.
 */
@Controller
@RequestMapping("/relation")
public class RelationController extends BaseController {

    @Resource
    private RelationService relationService;

    @Resource
    private UserService userService;


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse<List<UserModel>> createAttention(@RequestBody List<Long> users) {
        checkLogin();
        if (users == null || users.size() == 0) {
            return APIResponse.returnSuccess(Collections.<UserModel>emptyList());
        }
        return APIResponse.returnSuccess(relationService.createAttention(queryUserInfo().getUser()
                , Lists.newArrayList(userService.getUserByUserID(users).values())));
    }

    @RequestMapping(value = "/getAttention", method = RequestMethod.GET)
    @ResponseBody
    public APIResponse<List<Attention>> getAttention(@RequestParam long userid, @RequestParam long lastId, @RequestParam int size) {
        UserModel user = userService.getUserByUserID(userid);
        if (user == null) {
            return convertErrorCode(ERROR_USER_IS_NOT_EXIST);
        }
        return APIResponse.returnSuccess(relationService.getAttention(user, lastId, size, true));
    }

    @RequestMapping(value = "/getFollowers", method = RequestMethod.GET)
    @ResponseBody
    public APIResponse<List<Attention>> getFollowers(@RequestParam long userid, @RequestParam long lastId, @RequestParam int size) {
        UserModel user = userService.getUserByUserID(userid);
        if (user == null) {
            return convertErrorCode(ERROR_USER_IS_NOT_EXIST);
        }
        return APIResponse.returnSuccess(relationService.getAttention(user, lastId, size, false));
    }
}
