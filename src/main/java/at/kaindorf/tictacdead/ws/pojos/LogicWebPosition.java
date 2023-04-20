package at.kaindorf.tictacdead.ws.pojos;

import at.kaindorf.tictacdead.pojos.State;
import lombok.Data;

@Data
public class LogicWebPosition extends WebPosition{

    private State player;

    public LogicWebPosition(Message message, State player) {
        super(message);
        this.player = player;
    }

}
