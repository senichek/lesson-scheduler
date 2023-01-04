package com.lessonscheduler.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.lessonscheduler.models.User;
import com.lessonscheduler.models.Role;

public class SecurityUtils {
    public static User getLoggedInUser() {
        User loggedIn = new User();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserPrincipal principal = (UserPrincipal)authentication.getPrincipal();
            loggedIn.setId(principal.getId());
            loggedIn.setName(principal.getName());
            if (principal.getAuthorities().toString().contains("ADMIN")) {
                loggedIn.setRole(Role.ADMIN);
            } else {
                loggedIn.setRole(Role.USER);
            }
        }
        return loggedIn;
    }
}
