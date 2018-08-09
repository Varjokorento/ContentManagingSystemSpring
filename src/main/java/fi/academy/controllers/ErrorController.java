package fi.academy.controllers;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/error")
    public String renderErrorPage(HttpServletRequest httpServletRequest) {
        System.out.println("hello");
        int errorCode = getErrorCode(httpServletRequest);
        if(errorCode == 404) {
            return "404";
        } else if(errorCode == 500) {
            return "500";
        }
        return "index";
    }

    private int getErrorCode(HttpServletRequest httpServletRequest) {
        return (Integer) httpServletRequest.getAttribute("javax.servlet.error.status_code");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
