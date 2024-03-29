/*
    Author: Franz Koinegg
    TICTACDEAD
 */

package at.kaindorf.tictacdead.ws.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// See where the player placed the button, so the backend can validate (NOT USED ANYMORE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebPosition {
    public WebPosition(Message message) {
        String tokens[] = message.getXyz().split("");

        this.x = Integer.parseInt(tokens[0]);
        this.y = Integer.parseInt(tokens[1]);
        this.z = Integer.parseInt(tokens[2]);

    }
    private Integer x;
    private Integer y;
    private Integer z;

    private Integer player;
}
