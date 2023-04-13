package at.kaindorf.tictacdead.pojos;

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

    public String getPositionAsString(){
        return String.format("%d%d%d", this.xCoordinate, this.yCoordinate, this.zCoordinate);
    }

    public String getFirstLetterOfState(){
        return fieldState.toString().substring(0, 1);
    }

}
