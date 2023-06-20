/*
    Author: Franz Koinegg
    TICTACDEAD
 */

package at.kaindorf.tictacdead.ws.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

// Object for the connected Players
@AllArgsConstructor
@Data
public class ConnectedPlayers {

    private Boolean player1;
    private Boolean player2;
}
