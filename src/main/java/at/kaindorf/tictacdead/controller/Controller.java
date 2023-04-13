package at.kaindorf.tictacdead.controller;

import at.kaindorf.tictacdead.service.BackendLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/")
public class Controller {

    private final BackendLogic service;

    @Autowired
    public Controller(BackendLogic service) {
        this.service = service;
    }


}
