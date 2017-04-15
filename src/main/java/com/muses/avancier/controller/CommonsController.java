package com.muses.avancier.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CommonsController {
    
    @RequestMapping("/")
    public ModelAndView homepage(){
        return new ModelAndView("index");
    }

    /**
     * 登入处理
     * @return
     */
    @RequestMapping("/login")
    public ModelAndView login(){
        return new ModelAndView("login");
    }
    
    /**
     * 登出处理
     */
    @RequestMapping("/logout")
    public ModelAndView logoutView(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        
        return new ModelAndView("redirect:/");
    }
}
