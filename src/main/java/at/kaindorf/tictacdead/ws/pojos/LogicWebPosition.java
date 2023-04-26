package at.kaindorf.tictacdead.ws.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogicWebPosition {
    private Integer x;
    private Integer y;
    private Integer z;
    private Integer player;

    public LogicWebPosition(Message message) {
        String tokens[] = message.getXyz().split("");
        if (tokens[0].equals("-")){
            this.x = Integer.parseInt(tokens[1]) * -1;
            this.y = Integer.parseInt(tokens[2]);
            this.z = Integer.parseInt(tokens[3]);
        }
        else {
            this.x = Integer.parseInt(tokens[0]);
            this.y = Integer.parseInt(tokens[1]);
            this.z = Integer.parseInt(tokens[2]);
        }

        this.player = message.getPlayer();
    }

}
