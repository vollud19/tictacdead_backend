package at.kaindorf.tictacdead.ws.controller;

//@Controller
public class PlayerController {

//    stores which player is used already:
//    true means that the player is free to use
//
//    private Boolean player1 = true;
//    private Boolean player2 = true;
//
//    /* ToDo: Add number to Message to identify the Player (public Message) */
//
//    @MessageMapping("/2")
//    @SendTo("/player/1")
//    public WebPosition player1(Message message) throws Exception {
//        Thread.sleep(1000); // simulated delay
//        WebPosition webPosition = new WebPosition(message);
//        LogicWebPosition logicWebPosition = new LogicWebPosition(message, State.YELLOW);
//        /*
//         ToDo: Otto Game Logic bla bla bla
//         */
//
//
//        return webPosition;
//    }
//
//    @MessageMapping("/1")
//    @SendTo("/player/2")
//    public WebPosition player2(Message message) throws Exception {
//        Thread.sleep(1000); // simulated delay
//        WebPosition webPosition = new WebPosition(message);
//        LogicWebPosition logicWebPosition = new LogicWebPosition(message, State.RED);
//        /*
//         ToDo: Otto Game Logic bla bla bla
//         */
//        return webPosition;
//    }
//
//    @MessageMapping("/connectedPlayers")
//    @SendTo("/players")
//    public ConnectedPlayers players(ConnectedPlayers players) throws Exception {
//        Thread.sleep(1000); // simulated delay
//
//        /*if(!(players.getPlayer1() && players.getPlayer2())){
//            ConnectedPlayers cp = new ConnectedPlayers(player1, player2);
//            return cp;
//        }*/
//
//        if (!players.getPlayer1() /*&& players.getPlayer2() == null*/ ){
//            if (player1){
//                player1 = false;
//                ConnectedPlayers cp = new ConnectedPlayers(player1, player2);
//                return cp;
//            }
//        } else if (!players.getPlayer2() /*&& players.getPlayer1() == null*/) {
//            if (player2){
//                player2 = false;
//                ConnectedPlayers cp = new ConnectedPlayers(player1, player2);
//                return cp;
//            }
//        }
//        ConnectedPlayers cp = new ConnectedPlayers(player1, player2);
//        return cp;
//    }
//
//    @GetMapping("/usingPlayer/{usingPlayer}")
//    ResponseEntity usingPlayer(@PathVariable Integer usingPlayer) {
//        if (usingPlayer == 1){
//            if (!player1){
//                return new ResponseEntity<>("Player 1 is already in use!", HttpStatus.CONFLICT);
//            }
//            else{
//                player1 = false;
//                return new ResponseEntity<>("Player 1 is now in use!", HttpStatus.ACCEPTED);
//            }
//        }
//        else{
//            if (!player2){
//                return new ResponseEntity<>("Player 2 is already in use!", HttpStatus.CONFLICT);
//            }
//            else{
//                player2 = false;
//                return new ResponseEntity<>("Player 2 is now in use!", HttpStatus.ACCEPTED);
//            }
//        }
//    }
//
//    @PostMapping("/usingPlayer/{releasedPlayer}")
//    ResponseEntity releasePlayer(@PathVariable Integer releasedPlayer) {
//        if (releasedPlayer==1){
//            player1 = true;
//        }
//        else{
//            player2 = true;
//        }
//        return new ResponseEntity<>("Player "+ releasedPlayer +" is now released!", HttpStatus.OK);
//    }

}
