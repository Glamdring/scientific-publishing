package com.scipub.web;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scipub.dto.UserDetails;
import com.scipub.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Inject
    private UserService userService;
    
    /**
     * Method to handle the fcbkcomplete library, which has "tag" hardcoded as
     * param. Changing it is not preferable, because of possible upgrades
     *
     * @param tag
     * @param currentUser
     * @param model
     * @return the view with results
     */
    @RequestMapping("/autocompleteList")
    @ResponseBody
    public List<DefaultKeyValue> suggestUsersList(@RequestParam String tag) {

        List<UserDetails> details = userService.findUsers(tag);
        List<DefaultKeyValue> result = new ArrayList<DefaultKeyValue>(details.size());
        for (UserDetails detail : details) {
            // doing HTML stuff here, which is not particularly nice, but no
            // other way to do it
            String pic = detail.getSmallPhotoUri();
            if (pic == null) {
                pic = "http://www.gravatar.com/avatar/" + detail.getId() + "?s=16&d=identicon&r=PG";
            }

            String display = "<img class=\"userAutocompletePicture\" " + "src=\"" + pic + "\"/>"
                    + detail.getFirstName() + " " + detail.getLastName();

            result.add(new DefaultKeyValue(display, "id:" + detail.getId()));
        }

        return result;
    }
}
