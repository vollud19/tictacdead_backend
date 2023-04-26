package at.kaindorf.tictacdead.ws.controller;

import at.kaindorf.tictacdead.pojos.Position;
import at.kaindorf.tictacdead.ws.pojos.ConnectedPlayers;
import at.kaindorf.tictacdead.ws.pojos.LogicWebPosition;
import at.kaindorf.tictacdead.ws.pojos.Message;
import at.kaindorf.tictacdead.ws.pojos.WebPosition;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SingleWebSocketPlayerController {

//    stores which player is used already:
//    true means that the player is free to use
    private ConnectedPlayers connectedPlayers = new ConnectedPlayers(true, true);

    /* ToDo: Add number to Message to identify the Player (public Message) */
    @MessageMapping("/player")
    @SendTo("/player/msg")
    public LogicWebPosition game(Message message) throws Exception {
        Thread.sleep(1000); // simulated delay
        Position position = new Position(message);
        LogicWebPosition webPosition = new LogicWebPosition(message);
        /*
         ToDo: Otto Game Logic bla bla bla
         */
        return webPosition;
    }

    @PostMapping("/releasePlayer/{releasedPlayer}")
    ResponseEntity releasePlayer(@PathVariable Integer releasedPlayer) {
        if (releasedPlayer==1){
            connectedPlayers.setPlayer1(true);
        }
        else{
            connectedPlayers.setPlayer2(true);
        }
        return new ResponseEntity<>("Player "+ releasedPlayer +" is now released!", HttpStatus.OK);
    }

    @PostMapping("/usingPlayer/{usedPlayer}")
    ResponseEntity usePlayer(@PathVariable Integer usedPlayer) {
        if (usedPlayer==1){
            connectedPlayers.setPlayer1(false);
        }
        else{
            connectedPlayers.setPlayer2(false);
        }
        return new ResponseEntity<>("Player "+ usedPlayer +" is now in use!", HttpStatus.OK);
    }

    @GetMapping("/usedPlayers")
    ResponseEntity getUsedPlayer() {
        return new ResponseEntity<>(connectedPlayers, HttpStatus.OK);
    }
}
