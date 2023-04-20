package at.kaindorf.tictacdead.ws.pojos;

public class Message {

    private String placement;

    public Message() {
    }

    public Message(String placement) {
        this.placement = placement;
    }

    public String getPlacement() {
        return placement;
    }

    public void setPlacement(String name) {
        this.placement = name;
    }
}
