package com.huyendieu.parking.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class IndexController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void index(HttpServletResponse httpResponse) throws IOException {
        httpResponse.sendRedirect("/swagger-ui/index.html");
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<String> sayHello() {
        return new ResponseEntity<>("Hello world!", HttpStatus.OK);
    }
}
