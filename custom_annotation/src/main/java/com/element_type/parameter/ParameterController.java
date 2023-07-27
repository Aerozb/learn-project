package com.element_type.parameter;

import com.element_type.parameter.annotation.ParamValidAndLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ParameterController {

    @RequestMapping("/param")
    @ResponseBody
    public String hello(@ParamValidAndLog String name) {
        return "Hello " + name;
    }

}
