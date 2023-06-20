/*
    Author: Franz Koinegg
    TICTACDEAD
 */

package at.kaindorf.tictacdead.ws.controller;

import at.kaindorf.tictacdead.pojos.Position;
import at.kaindorf.tictacdead.service.BackendLogic;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@CrossOrigin(origins = "*")
public class SingleWebSocketPlayerController {

//    stores which player is used already:
//    true means that the player is free to use
    private ConnectedPlayers connectedPlayers = new ConnectedPlayers(true, true);

    private Integer turn = 1;

    /* ToDo: Move the turn conditions to the top (check before validate) */
    @MessageMapping("/player")
    @SendTo("/player/msg")
    public LogicWebPosition game(Message message) throws Exception {
        System.out.println("Message: "+ message);
        Position position = new Position(message);
        LogicWebPosition webPosition = new LogicWebPosition(message);

        if (message.getXyz().equals("-203") || message.getXyz().equals("-202")){
            return webPosition;
        }
        else if (turn%2 == 1){
            turn++;
            return webPosition;
        }
        /*else if(turn%2 != 1 && message.getPlayer() == 2){
            turn++;
            return webPosition;
        }*/
        else{
            System.out.println("Validate:");
            webPosition = BackendLogic.getTheInstance().validate(position);
            return webPosition;
        }
    }

    BackendLogic bl = BackendLogic.getTheInstance();

    // Releases the Player, so that he can rejoin the game again
    @PostMapping("/releasePlayer/{releasedPlayer}")
    ResponseEntity releasePlayer(@PathVariable Integer releasedPlayer) {
        if (releasedPlayer==1) {
            connectedPlayers.setPlayer1(true);
            bl.fillBoardWithBlankPositions();
            bl.printBoard();
        }
        else{
            connectedPlayers.setPlayer2(true);
        }
        return new ResponseEntity<>("Player "+ releasedPlayer +" is now released!", HttpStatus.OK);
    }

    // Send the currently Used player from the frontend to see which player is joined
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

    // Get all used Players and return a response
    @GetMapping("/usedPlayers")
    ResponseEntity getUsedPlayer() {
        return new ResponseEntity<>(connectedPlayers, HttpStatus.OK);
    }
}
