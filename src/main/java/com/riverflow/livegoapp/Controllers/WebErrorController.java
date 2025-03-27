
package com.riverflow.livegoapp.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";
    @Override
    public String getErrorPath() {

        return ERROR_PATH;
    }

    @RequestMapping("/error")
    public String handleError() {
        return "index.html";
    }

}
