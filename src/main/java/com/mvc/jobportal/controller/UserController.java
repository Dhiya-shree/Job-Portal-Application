package com.mvc.jobportal.controller;

import com.mvc.jobportal.entity.profile.Users;
import com.mvc.jobportal.entity.profile.UsersType;
import com.mvc.jobportal.services.user.UsersService;
import com.mvc.jobportal.services.user.UsersTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {
    private final UsersTypeService usersTypeService;
    private final UsersService usersService;

    public UserController(UsersTypeService usersTypeService, UsersService usersService) {
        this.usersTypeService = usersTypeService;
        this.usersService = usersService;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }

        return "redirect:/";
    }

    @GetMapping("/register")
    public String userRegister( Model model){
        List<UsersType> usersTypes=usersTypeService.getAll();
        model.addAttribute("getAllTypes",usersTypes);
        model.addAttribute("user",new Users());
        return "register";
    }

    @PostMapping("/register/new")
    public String userRegisterPost(@Valid Users user,Model model){
        boolean duplicate= usersService.findUserByEmail(user.getEmail()).isPresent();
        if(duplicate){
            model.addAttribute("error","Email already exists");
                    List<UsersType> usersTypes=usersTypeService.getAll();
                    model.addAttribute("getAllTypes",usersTypes);
                    model.addAttribute("user",new Users());
                    return "register";
        }
        usersService.addNew(user);
        return "redirect:/dashboard/";
    }

}
