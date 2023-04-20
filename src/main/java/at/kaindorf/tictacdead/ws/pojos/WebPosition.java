package at.kaindorf.tictacdead.ws.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebPosition {
    public WebPosition(Message message) {
        String tokens[] = message.getPlacement().split("");

        this.x = Integer.parseInt(tokens[0]);
        this.y = Integer.parseInt(tokens[1]);
        this.z = Integer.parseInt(tokens[2]);

    }
    private Integer x;
    private Integer y;
    private Integer z;
}
