package com.huyendieu.parking.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huyendieu.parking.entities.TestEntity;
import com.huyendieu.parking.repositories.ITestRepository;

@RestController
public class HelloController {

    @Autowired
    private ITestRepository _testRepository;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<TestEntity>> custom() {
        return new ResponseEntity<>(_testRepository.findAll(), HttpStatus.OK);
    }
}
