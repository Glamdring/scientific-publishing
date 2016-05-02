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

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.googlecode.googleplus.Plus;
import com.googlecode.googleplus.model.person.Person;
import com.scipub.dto.RegistrationDto;
import com.scipub.model.User;
import com.scipub.service.UserService;

@Controller
public class AuthenticationController {
    public static final String REDIRECT_AFTER_LOGIN = "redirectAfterLogin";

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Inject
    private ProviderSignInController signInController;
    @Inject
    private SocialSignInAdapter signInAdapter;
    @Inject
    private UserService userService;
    @Inject
    private UserContext context;

    private RestTemplate restTemplate = new RestTemplate();
    private EmailValidator emailValidator = new EmailValidator();

    public AuthenticationController() {
        HttpMessageConverter<?> formHttpMessageConverter = new FormHttpMessageConverter();
        HttpMessageConverter<?> stringHttpMessageConverternew = new StringHttpMessageConverter();
        restTemplate.getMessageConverters().add(formHttpMessageConverter);
        restTemplate.getMessageConverters().add(stringHttpMessageConverternew);
    }

    @RequestMapping(value="/signin/{providerId}", method=RequestMethod.GET, params="home") //param to discriminate from the cancellation url (ugly, I know)
    public RedirectView signin(@PathVariable String providerId, @RequestParam(required=false) String redirectUri, NativeWebRequest request, HttpSession session) {
        if (redirectUri != null) {
            session.setAttribute(REDIRECT_AFTER_LOGIN, redirectUri);
        }
        return signInController.signIn(providerId, request);
    }

    @RequestMapping("/signin")
    public String signinPage() {
        return "signin";
    }
    
    @RequestMapping("/socialSignUp")
    public String socialSignupPage(@RequestParam(required=false) String email, NativeWebRequest request, Model model) {
        ProviderSignInAttempt attempt = (ProviderSignInAttempt) request.getAttribute(ProviderSignInAttempt.class.getName(), RequestAttributes.SCOPE_SESSION);
        if (attempt == null && StringUtils.isEmpty(email)) {
            return "redirect:/";
        }
        User user = new User();
        if (attempt != null) {
            Object api = attempt.getConnection().getApi();
            if (api instanceof Facebook) {
                FacebookProfile profile = ((Facebook) api).userOperations().getUserProfile();
                user.setEmail(profile.getEmail());
                user.setFirstName(profile.getFirstName());
                user.setLastName(profile.getLastName());
            } else if (api instanceof Twitter) {
                TwitterProfile profile = ((Twitter) api).userOperations().getUserProfile();
                String[] names = profile.getName().split(" ");
                if (names.length == 2) {
                    user.setFirstName(names[0]);
                    user.setLastName(names[1]);
                } else if (names.length == 3) {
                    user.setFirstName(names[0]);
                    user.setMiddleName(names[1]);
                    user.setLastName(names[2]);
                }
            } else if (api instanceof Plus) {
                Person person = ((Plus) api).getPeopleOperations().get("me");
                user.setFirstName(person.getName().getGivenName());
                user.setMiddleName(person.getName().getMiddleName());
                user.setLastName(person.getName().getFamilyName());
                if (!person.getEmails().isEmpty()) {
                    user.setEmail(person.getEmails().iterator().next().getValue());
                }
            } else if (api instanceof LinkedIn) {
               LinkedInProfile profile = ((LinkedIn) api).profileOperations().getUserProfile();
               user.setFirstName(profile.getFirstName());
               user.setLastName(profile.getLastName());
               user.setEmail(profile.getEmailAddress());
            }
        } else {
            user.setEmail(email);
            model.addAttribute("type", "Persona");
        }
        model.addAttribute("user", user);
        return "socialSignup";
    }

    @RequestMapping("/social/completeRegistration")
    public String completeRegistration(RegistrationDto registration, @RequestParam String type,
            NativeWebRequest request, HttpSession session, Model model) {

        if (!emailValidator.isValid(registration.getEmail(), null)) {
            return "redirect:/?message=Invalid email. Try again";
        }

        // if the session has expired for a fb/tw registration (i.e. attempt is null), do not proceed - otherwise inconsistent data is stored
        ProviderSignInAttempt attempt = (ProviderSignInAttempt) request.getAttribute(ProviderSignInAttempt.class.getName(), RequestAttributes.SCOPE_SESSION);
        if (attempt != null) {
            registration.setConnection(attempt.getConnection());
        }
        if (attempt != null || "Persona".equals(type)){
            User user = userService.completeUserRegistration(registration);
            signInAdapter.signIn(user, (HttpServletResponse) request.getNativeResponse(), true);
        }
        String redirectUri = (String) session.getAttribute(REDIRECT_AFTER_LOGIN);
        if (redirectUri != null) {
            return "redirect:" + redirectUri;
        }
        return "redirect:/";
    }

    @RequestMapping("/persona/auth")
    @ResponseBody
    public String authenticateWithPersona(@RequestParam String assertion,
            @RequestParam boolean userRequestedAuthentication, HttpServletRequest request,
            HttpServletResponse httpResponse, Model model)
            throws IOException {
        if (context.getUser() != null) {
            return "";
        }
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("assertion", assertion);
        params.add("audience", request.getScheme() + "://" + request.getServerName() + ":" + (request.getServerPort() == 80 ? "" : request.getServerPort()));
        PersonaVerificationResponse response = restTemplate.postForObject("https://verifier.login.persona.org/verify", params, PersonaVerificationResponse.class);
        if (response.getStatus().equals("okay")) {
            User user = userService.getUserByEmail(response.getEmail());
            if (user == null && userRequestedAuthentication) {
                return "/socialSignUp?email=" + response.getEmail();
            } else if (user != null){
                if (userRequestedAuthentication || user.isLoginAutomatically()) {
                    signInAdapter.signIn(user, httpResponse, true);
                    return "/";
                } else {
                    return "";
                }
            } else {
                return ""; //in case this is not a user-requested operation, do nothing
            }
        } else {
            logger.warn("Persona authentication failed due to reason: " + response.getReason());
            throw new IllegalStateException("Authentication failed");
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        session.invalidate();
        Cookie cookie = WebUtils.getCookie(request, SocialSignInAdapter.AUTH_TOKEN_COOKIE_NAME);
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setDomain(com.scipub.web.util.WebUtils.ROOT_DOMAIN);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        cookie = WebUtils.getCookie(request, SocialSignInAdapter.AUTH_TOKEN_SERIES_COOKIE_NAME);
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setDomain(com.scipub.web.util.WebUtils.ROOT_DOMAIN);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return "redirect:/";
    }
    @RequestMapping("/digestEmailUnsubscribe/{id}")
    public String unsubscribe(@PathVariable long id, @RequestParam String hash) {
        userService.unsubscribe(id, hash);
        return "redirect:/?message=Successfully unsubscribed";
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    @SuppressWarnings("unused")
    private static class PersonaVerificationResponse {
        private String status;
        private String email;
        private String reason;
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getReason() {
            return reason;
        }
        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
