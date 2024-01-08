package com.example.shopmebe.controller;

import com.example.shopmebe.exception.error.CustomStatusCode;
import com.example.shopmebe.exception.error.CustomStatusCodeCommand;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class MyCustomErrorController implements ErrorController {


//    @GetMapping("/error")
//    public ModelAndView renderErrorPage(HttpServletRequest httpServletRequest) {
//        ModelAndView errorPage = new ModelAndView("error/errorPage");
//        int httpErrorCode = getErrorCode(httpServletRequest);
//
//        CustomStatusCodeCommand statusCodeCommand = new CustomStatusCodeCommand();
//        CustomStatusCode statusCode = statusCodeCommand.createStatusCode(httpErrorCode);
//
//        errorPage.addObject("errorMsg", statusCode.getMsg());
//        return errorPage;
//    }
//
//    private int getErrorCode(HttpServletRequest httpRequest) {
//        return httpRequest.getAttribute("javax.servlet.error.status_code") != null
//                ? (Integer) httpRequest.getAttribute("javax.servlet.error.status_code")
//                : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
//    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ModelAndView handleException(Exception e, HttpServletRequest request) {
//        ModelAndView modelAndView = new ModelAndView("errors/errorPage");
//
//        int httpErrorCode = getErrorCode(request);
//        CustomStatusCodeCommand statusCodeCommand = new CustomStatusCodeCommand();
//        CustomStatusCode statusCode = statusCodeCommand.createStatusCode(httpErrorCode);
//
//        modelAndView.addObject("errorMsg", e.getMessage()); // Add the actual exception message if needed
//        modelAndView.addObject("customErrorMsg", statusCode.getMsg());
//        return modelAndView;
//    }


    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public ModelAndView renderErrorPage(HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("errors/errorPage");

//        WebRequest webRequest = new ServletWebRequest(response);

//        Throwable error = errorAttributes.getError(webRequest);
//        int httpErrorCode = getStatusCode(error);
        CustomStatusCodeCommand statusCodeCommand = new CustomStatusCodeCommand();
        CustomStatusCode statusCode = statusCodeCommand.createStatusCode(response.getStatus());

        modelAndView.addObject("errorMsg", statusCode.getMsg());
        return modelAndView;
    }

//    private int getErrorCode(HttpServletRequest request) {
//        return request.getAttribute("javax.servlet.error.status_code") != null
//                ? (Integer) request.getAttribute("jakarta.servlet.error.status_code")
//                : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
//    }

    private int getStatusCode(Throwable error) {
        return (error instanceof ResponseStatusException)
                ? ((ResponseStatusException) error).getStatusCode().value()
                : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
