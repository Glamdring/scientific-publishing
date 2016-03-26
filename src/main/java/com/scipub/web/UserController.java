/**
 * Scientific publishing
 * Copyright (C) 2015-2016  Bozhidar Bozhanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
