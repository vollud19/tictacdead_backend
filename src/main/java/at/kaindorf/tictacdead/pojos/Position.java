/*
    Author: Otto Alwinger
    TICTACDEAD
 */

package at.kaindorf.tictacdead.pojos;

import at.kaindorf.tictacdead.ws.pojos.Message;
import at.kaindorf.tictacdead.ws.pojos.WebPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Position {
    private Integer xCoordinate;
    private Integer yCoordinate;
    private Integer zCoordinate;
    private State fieldState;


    public Position(Message message) {
        String tokens[] = message.getXyz().split("");
        if (tokens[0].equals("-")){
            this.xCoordinate = Integer.parseInt(tokens[1]) * -1;
            this.yCoordinate = Integer.parseInt(tokens[2]);
            this.zCoordinate = Integer.parseInt(tokens[3]);
        }
        else {
            this.xCoordinate = Integer.parseInt(tokens[0]);
            this.yCoordinate = Integer.parseInt(tokens[1]);
            this.zCoordinate = Integer.parseInt(tokens[2]);
        }

        if (message.getPlayer() ==1){
            this.fieldState = State.YELLOW;
        }
        else{
            this.fieldState = State.RED;
        }
    }

    public String getPositionAsString(){
        return String.format("%d%d%d", this.xCoordinate, this.yCoordinate, this.zCoordinate);
    }


    public String getFirstLetterOfState(){
        return fieldState.toString().substring(0, 1);
    }


}
