/*
    Author: Otto Alwinger
    TICTACDEAD
 */

package at.kaindorf.tictacdead.pojos;

public enum State {
    BLANK(0), YELLOW(1), RED(2);

    public final Integer value;

    State(Integer value) {
        this.value = value;
    }
}
