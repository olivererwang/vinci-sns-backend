package com.vinci.backend.web.controller;

import com.google.common.collect.Lists;
import com.vinci.backend.domain.feed.model.FeedModel;
import com.vinci.backend.domain.feed.service.FeedService;
import com.vinci.backend.domain.relations.model.Attention;
import com.vinci.backend.domain.relations.service.RelationService;
import com.vinci.backend.domain.user.model.UserModel;
import com.vinci.backend.domain.user.service.UserService;
import com.vinci.backend.web.util.BaseController;
import com.vinci.common.base.api.APIResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.vinci.backend.web.WebConstants.*;

/**
 * Created by tim@vinci on 15-2-3.
 */
@Controller
@RequestMapping("/feed")
public class FeedController extends BaseController {
    @Resource
    private RelationService relationService;

    @Resource
    private UserService userService;

    @Resource
    private FeedService feedService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse<FeedModel> createFeed(@RequestBody FeedModel feed) {
        checkLogin();
        if (feed == null || StringUtils.isEmpty(feed.getContent()) || StringUtils.isEmpty(feed.getFeedType())) {
            return convertErrorCode(ERROR_FEED_IS_NULL);
        }
        return APIResponse.returnSuccess(feedService.insertFeed(queryUserInfo().getUser(), feed));

    }

    @RequestMapping(value = "/getFeed", method = RequestMethod.GET)
    @ResponseBody
    public APIResponse<List<FeedModel>> getFeed(@RequestParam long userid, @RequestParam long lastId, @RequestParam int size) {
        UserModel user = userService.getUserByUserID(userid);
        if (user == null) {
            return convertErrorCode(ERROR_USER_IS_NOT_EXIST);
        }
        return APIResponse.returnSuccess(feedService.getUserFeed(user, lastId, size));
    }

    @RequestMapping(value = "/getTimeline", method = RequestMethod.GET)
    @ResponseBody
    public APIResponse<List<FeedModel>> getTimeline(@RequestParam long lastId, @RequestParam int size) {
        checkLogin();
        return APIResponse.returnSuccess(feedService.getUserTimeline(queryUserInfo().getUser(), lastId, size));
    }
}
