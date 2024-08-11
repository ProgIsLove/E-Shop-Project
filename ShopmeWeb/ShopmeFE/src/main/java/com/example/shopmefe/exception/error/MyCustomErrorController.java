package com.example.shopmefe.exception.error;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class MyCustomErrorController implements ErrorController {


    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public ModelAndView renderErrorPage(HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("errors/errorPage");

        CustomStatusCodeCommand statusCodeCommand = new CustomStatusCodeCommand();
        CustomStatusCode statusCode = statusCodeCommand.createStatusCode(response.getStatus());

        modelAndView.addObject("errorMsg", statusCode.getMsg());
        return modelAndView;
    }
}
