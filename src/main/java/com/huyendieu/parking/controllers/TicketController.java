package com.huyendieu.parking.controllers;

import com.huyendieu.parking.model.request.SearchTicketRequestModel;
import com.huyendieu.parking.model.request.TicketRequestModel;
import com.huyendieu.parking.model.response.TicketListResponseModel;
import com.huyendieu.parking.model.response.base.ErrorResponseModel;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.model.response.common.base.ListComboboxResponseModel;
import com.huyendieu.parking.services.TicketService;
import com.huyendieu.parking.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/list")
    public ResponseEntity<?> list(Authentication authentication, @Valid @RequestBody SearchTicketRequestModel requestModel) {
        try {
            String userName = UserUtils.getUserName(authentication);
            TicketListResponseModel responseModel = ticketService.getTickets(userName, requestModel);
            return new ResponseEntity<>(new SuccessfulResponseModel(responseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity<>(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> create(Authentication authentication, @Valid @RequestBody TicketRequestModel requestModel) {
        try {
            if (!UserUtils.isParkingAreaRole(authentication)) {
                return new ResponseEntity<>(new ErrorResponseModel(), HttpStatus.FORBIDDEN);
            }
            String userName = UserUtils.getUserName(authentication);
            String id = ticketService.create(userName, requestModel);
            return new ResponseEntity<>(new SuccessfulResponseModel(id), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity<>(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("")
    public ResponseEntity<?> update(Authentication authentication, @Valid @RequestBody TicketRequestModel requestModel) {
        try {
            if (!UserUtils.isParkingAreaRole(authentication)) {
                return new ResponseEntity<>(new ErrorResponseModel(), HttpStatus.FORBIDDEN);
            }
            String userName = UserUtils.getUserName(authentication);
            String id = ticketService.update(userName, requestModel);
            return new ResponseEntity<>(new SuccessfulResponseModel(id), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity<>(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(Authentication authentication, @PathVariable String id) {
        try {
            if (!UserUtils.isParkingAreaRole(authentication)) {
                return new ResponseEntity<>(new ErrorResponseModel(), HttpStatus.FORBIDDEN);
            }
            String ticketId = ticketService.delete(id);
            return new ResponseEntity<>(new SuccessfulResponseModel(ticketId), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity<>(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-types")
    public ResponseEntity<?> getTypes() {
        try {
            ListComboboxResponseModel responseModel = ticketService.getTypes();
            return new ResponseEntity<>(new SuccessfulResponseModel(responseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity<>(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }
}
