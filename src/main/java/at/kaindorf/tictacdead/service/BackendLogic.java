/*
    Author: Otto Allwinger
    TICTACDEAD
 */

package at.kaindorf.tictacdead.service;

import at.kaindorf.tictacdead.pojos.Position;
import at.kaindorf.tictacdead.pojos.State;
import at.kaindorf.tictacdead.ws.pojos.LogicWebPosition;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BackendLogic {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String GREEN_UNDERLINED = "\033[4;32m";
    public static final String PURPLE = "\033[0;35m";

    //Coordinates X / Y / Z; Represents the board; Starts with 0

    private Position[][][] board = new Position[4][4][4];

    /*ToDo: remove this temporal draw detection
     *  Todo: do undo*/
    private Integer moves = 0;
    private static BackendLogic backendLogic;

    private Position oldPosition;
    private Random randomNumber;

    public LogicWebPosition validate(Position position) {

        printBoard();
//        Check if the move is valid
        if (placePiece(position)) {
//            Convert X, Y, Z to one integer:
            Integer move = position.getXCoordinate() * 100 + position.getYCoordinate() * 10 + position.getZCoordinate();
            System.out.println(move);
//            Check if the move leads to a win
            if (checkWinBoolean(checkWin(move))) {
                return new LogicWebPosition(-2, 0, 0, position.getFieldState().value);
            }
//            check if Winning is possible
            else if (checkDraw()) {
                return new LogicWebPosition(-4, 0, 9, 0);
            }
//            Simply place the piece
            else {
                return new LogicWebPosition(position.getXCoordinate(), position.getYCoordinate(),
                        position.getZCoordinate(), position.getFieldState().value);
            }
        }
//        Return invalid placement error Code
        else {
            return new LogicWebPosition(-4, 0, 3, position.getFieldState().value);
        }


    }


    public static BackendLogic getTheInstance() {
        if (backendLogic == null) {
            backendLogic = new BackendLogic();
        }
        return backendLogic;
    }

    /**
     * Fills the board with Positions; Coordinates and States (Initialize board)
     */
    public void fillBoardWithBlankPositions() {
        for (int z = 0; z < 4; z++) {
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    board[x][y][z] = new Position(x, y, z, State.BLANK);
                }
            }
        }
    }

    /**
     * Prints the board onto console, to see coordinates and states of each field
     */
    public void printBoard() {
        StringBuilder boardString = new StringBuilder("|---|0.Layer|---|\t\t|---|1.Layer|---|\t\t|---|2.Layer|---|\t\t|---|3.Layer|---|\n");
        StringBuilder boardStateString = new StringBuilder(boardString.toString());

        for (int y = 0; y < 4; y++) {
            for (int z = 0; z < 4; z++) {
                for (int x = 0; x < 4; x++) {
                    String boardStateShort = board[x][y][z].getFieldState().toString().substring(0, 3);
                    boardString.append(String.format("|%s", board[x][y][z].getPositionAsString()));
                    boardStateString.append(String.format("|%s", boardStateShort.equals("BLA") ? "   " : boardStateShort));
                }
                boardString.append("|\t\t");
                boardStateString.append("|\t\t");
            }
            boardString.append("\n|---|---|---|---|\t\t|---|---|---|---|\t\t|---|---|---|---|\t\t|---|---|---|---|\n");
            boardStateString.append("\n|---|---|---|---|\t\t|---|---|---|---|\t\t|---|---|---|---|\t\t|---|---|---|---|\n");
        }

        System.out.println(boardString);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------------\n");
        System.out.println(boardStateString);
    }

    public void launcher() {

        State state;
        Scanner scanner = new Scanner(System.in);
        Integer coordinates = null;
        String tmp;
        Integer isPlayingAgainstAi = 0;


        // Initialize board with printout
        fillBoardWithBlankPositions();  /*ToDo: add to Constructor*/
        printBoard();

        System.out.println("Do you want to play against ai? Enter 0 or 1: ");
        isPlayingAgainstAi = scanner.nextInt();
        if (isPlayingAgainstAi == 0) {

            // Goes through as many times as the game has fields
            // In the for loop each player can place their pieces and after each placement a win check occurs
            // After for the game is automatically a draw
            for (int cnt = 1; cnt <= (4 * 4 * 4); cnt++) {

                if (cnt % 2 == 0) {
                    state = State.YELLOW;
                    System.out.println(ANSI_YELLOW + "Yellows Turn" + ANSI_RESET);
                } else {
                    state = State.RED;
                    System.out.println(ANSI_RED + "Reds Turn" + ANSI_RESET);
                }

                do {
                    System.out.println(PURPLE + "Enter coordinates in format XYZ: " + ANSI_RESET);
                    tmp = scanner.next();
                    coordinates = Integer.parseInt(tmp);
                }
                while (
                        (tmp.length() != 3) || !placePiece(new Position(
                                        coordinates / 100,
                                        (coordinates / 10) % 10,
                                        coordinates % 10, state
                                )
                        )
                );

                if (checkWinBoolean(checkWin(coordinates))) {
                    System.out.println(GREEN_BOLD + GREEN_UNDERLINED + state + " won" + ANSI_RESET);
                    return;
                }
            }
            System.out.println("The game ended in a draw");
        } else {
            System.out.println("Choose the difficulty between 1 - 3: ");
            Integer difficulty = scanner.nextInt();
            Position tmpPos;

            for (int cnt = 1; cnt <= (4 * 4 * 4); cnt++) {
                if (cnt % 2 == 0) {
                    state = State.YELLOW;
                    System.out.println(ANSI_YELLOW + "Yellows Turn" + ANSI_RESET);


                    do {
                        System.out.println(PURPLE + "Enter coordinates in format XYZ: " + ANSI_RESET);
                        tmp = scanner.next();
                        coordinates = Integer.parseInt(tmp);
                        oldPosition = new Position(
                                coordinates / 100,
                                (coordinates / 10) % 10,
                                coordinates % 10, state
                        );
                    }
                    while ((tmp.length() != 3) || !placePiece(oldPosition));

                    if (checkWinBoolean(checkWin(coordinates))) {
                        System.out.println(GREEN_BOLD + GREEN_UNDERLINED + state + " won" + ANSI_RESET);
                        return;
                    }
                } else {
                    do {
                        tmpPos = placeRandomPositionInField(oldPosition, difficulty);
                        tmpPos.setFieldState(State.RED);
                    }
                    while (!placePiece(tmpPos));

                    if (checkWinBoolean(checkWin(tmpPos.getXCoordinate(), tmpPos.getYCoordinate(), tmpPos.getZCoordinate()))) {
                        System.out.println(GREEN_BOLD + GREEN_UNDERLINED + "Red won" + ANSI_RESET);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Places the piece onto the board
     * Additionally it checks if the field is empty
     *
     * @param piece Or X/Y/Z coordinates TODO has to be discussed
     * @return boolean to check if placement was successful
     */
    public boolean placePiece(Position piece) {
        boolean succeeded = false;
        Integer x = piece.getXCoordinate();
        Integer y = piece.getYCoordinate();
        Integer z = piece.getZCoordinate();

        if ((x < 0 || x > 3) || (y < 0 || y > 3) || (z < 0 || z > 3)) {
            return false;
        }

        Position boardPosition = board[x][y][z];

        if (boardPosition.getFieldState().equals(State.BLANK)) {
            boardPosition.setFieldState(piece.getFieldState());
            succeeded = true;
        }
        printBoard();

        return succeeded;
    }





    // Red is here always ai

//    private boolean isMoveLeft(){
//        for (int x = 0; x < 4; x++){
//            for (int y = 0; y < 4; y++){
//                for (int z = 0; z < 4; z++){
//                    if (board[x][y][z].getFieldState().equals(State.BLANK)){
//                       return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
//    private Position bestMove() {
//        Integer bestScore = Integer.MIN_VALUE;
//        Integer score;
//        Position move = null;
//
//        for (int x = 0; x < 4; x++){
//            for (int y = 0; y < 4; y++){
//                for (int z = 0; z < 4; z++){
//                    if (board[x][y][z].getFieldState().equals(State.BLANK)){
//                        board[x][y][z].setFieldState(State.RED);
//                        score = minimax(board, 0, false); // Des koennte der Fehler sein
//                        board[x][y][z].setFieldState(State.BLANK);
//                        if (score > bestScore){
//                            bestScore = score;
//                            move = new Position(x,y,z, State.RED);
//                        }
//                    }
//                }
//            }
//        }
//        return move;
//    }

    // TODO
    //  Check win logik ueberarbeiten damit nicht nur boolean zurueckgegeben wird
    //  So ueberarbeiten dass, nich mehr boolean sondern state zurueckgegeben wird

//    private Integer getScore(State state){
//        if (state.equals(State.RED)) return 1;
//        else return -1;
//    }


    // Todo review this code and correct it
//    private Integer minimax(Position[][][] board, Integer depth, Boolean isMaximizing) {
//        Integer score = 0;
//        State state = State.BLANK;
//
//        for (int x = 0; x < 4; x++){
//            for (int y = 0; y < 4; y++){
//                for (int z = 0; z < 4; z++){
//                    state = checkWin(x, y, z);
//                    if (!state.equals(State.BLANK)){
//                        return score  = getScore(state);
//                    }
//                }
//            }
//        }
//
//        if (state.equals(State.BLANK)) return 0;
//
//        if (isMoveLeft() == false) return 0;
//
//        if (isMaximizing)
//        {
//            int best = Integer.MIN_VALUE;
//
//            // Traverse all cells
//            for (int x = 0; x < 4; x++){
//                for (int y = 0; y < 4; y++){
//                    for (int z = 0; z < 4; z++){
//                        if (board[x][y][z].equals(State.BLANK))
//                        {
//                            // Make the move
//                            board[x][y][z].setFieldState(State.RED);
//                            best = Math.max(best ,minimax(board, depth + 1, !isMaximizing)); // Des koennte der Fehler sein
//                            board[x][y][z].setFieldState(State.BLANK);
//                        }
//                    }
//                }
//            }
//            return best;
//        }else {
//            int best = Integer.MAX_VALUE;
//
//            // Traverse all cells
//            for (int x = 0; x < 4; x++){
//                for (int y = 0; y < 4; y++){
//                    for (int z = 0; z < 4; z++){
//                        if (board[x][y][z].equals(State.BLANK))
//                        {
//                            // Make the move
//                            board[x][y][z].setFieldState(State.YELLOW);
//                            best = Math.min(best ,minimax(board, depth + 1, !isMaximizing)); // Des koennte der Fehler sein
//                            board[x][y][z].setFieldState(State.BLANK);
//                        }
//                    }
//                }
//            }
//            return best;
//        }
//
//    }

    //////////////////////////////

    private BackendLogic() {
        fillBoardWithBlankPositions();
    }

    public Position placeRandomPositionInField(Position oldPosition, Integer option) {

        switch (option) {
            case 1:
                return getTotallyRandomPosition(oldPosition);
            case 2:
                return getRandomPositionWithin2(oldPosition);
//            case 3:
//                return null;

            default:
                throw new RuntimeException();
        }

    }

    private Position getTotallyRandomPosition(Position oldPosition) {
        randomNumber = new Random();
        return new Position(randomNumber.nextInt(3), randomNumber.nextInt(3), randomNumber.nextInt(3),
                oldPosition.getFieldState() == State.YELLOW ? State.RED : State.YELLOW);
    }

    private Position getRandomPositionWithin2(Position oldPosition) {
        randomNumber = new Random();

        return new Position(oldPosition.getXCoordinate() + randomNumber.nextInt(-2, 2),
                oldPosition.getYCoordinate() + randomNumber.nextInt(-2, 2),
                oldPosition.getZCoordinate() + randomNumber.nextInt(-2, 2),
                oldPosition.getFieldState() == State.YELLOW ? State.RED : State.YELLOW);

    }

    public boolean checkWinBoolean(State state) {
        if (!state.equals(State.BLANK)) return true;
        return false;
    }


    /**
     * Method that determines after a placement if the affected rows fulfill the win condition
     * It calls 2 other methods. (LayerCheckWin, MultiLayerCheckWin)
     *
     * @param coordinates
     * @return boolean to check if there is a winner
     */
    public State checkWin(Integer coordinates) {
        Integer x = coordinates / 100;
        Integer y = (coordinates / 10) % 10;
        Integer z = coordinates % 10;

        System.out.println(x);
        System.out.println(y);
        System.out.println(z);

        State state = State.BLANK;

        state = layerWinCheck(x, y, z);
        if (!state.equals(State.BLANK)) return state;
        state = multiLayerWinCheck(x, y);

        return state;
    }

    public State checkWin(Integer x, Integer y, Integer z) {

//        System.out.println(x);
//        System.out.println(y);
//        System.out.println(z);


        State state = State.BLANK;

        state = layerWinCheck(x, y, z);
        if (!state.equals(State.BLANK)) return state;
        state = multiLayerWinCheck(x, y);

        return state;
    }

    public boolean checkDraw() {
        return (moves++ == 64) ? true : false;
    }


    private State layerWinCheck(Integer x, Integer y, Integer z) {
        State state = State.BLANK;

        state = checkXAxisOnY(y, z);
        if (!state.equals(State.BLANK)) return state;
        state = checkYAxisOnX(x, z);
        if (!state.equals(State.BLANK)) return state;
        state = checkXYDiagonal(x, y, z);
        ;

        return state;
    }


    private State multiLayerWinCheck(Integer x, Integer y) {
        State state = State.BLANK;

        state = checkZAxisOnXY(x, y);
        if (!state.equals(State.BLANK)) return state;
        state = checkZYDiagonalOnX(x);
        if (!state.equals(State.BLANK)) return state;
        state = checkZXDiagonalOnY(y);
        if (!state.equals(State.BLANK)) return state;
        state = checkZDiagonalWithXY(x, y);

        return state;
    }

    private State checkZDiagonalWithXY(Integer x, Integer y) {

        boolean hasToBeReversed;

//         This argument checks if Position is at the corners (x == 0 || x == 3 && y == 0 || y == 3)
//         (x == 1 && y == 1), (x == 1 && y == 2),(x == 2 && y == 1), (x == 2 && y == 2)),
//         Checks if position is this field

        if ((
                ((x == 0 && y == 0) || (x == 3 && y == 3))
                        || (x == 1 && y == 1)
                        || (x == 2 && y == 2)
        )) {
            hasToBeReversed = false;
        } else if (
                ((x == 0 && y == 3) || (x == 3 && y == 0))
                        || (x == 1 && y == 2)
                        || (x == 2 && y == 1)
        ) {
            hasToBeReversed = true;
        } else return State.BLANK;


        StringBuilder checkWin = new StringBuilder();
        int y1 = 0;
        int z1 = 0;

        if (hasToBeReversed) {
            for (int x1 = 3; x1 >= 0; x1--) {
//                System.out.println("X: " + x1 + " Y: " + y1);
                checkWin.append(board[x1][y1++][z1++].getFirstLetterOfState());
            }

            if (!(checkWin.toString().equals("RRRR") || checkWin.toString().equals("YYYY"))) {
                checkWin = new StringBuilder();
                y1 = 0;
                z1 = 3;

                for (int x1 = 0; x1 < 4; x1++) {
                    checkWin.append(board[x1][y1++][z1--].getFirstLetterOfState());
                }
            }
        } else {
            checkWin = new StringBuilder();
            y1 = 0;
            z1 = 0;

            for (int x1 = 0; x1 < 4; x1++) {
                checkWin.append(board[x1][y1++][z1++].getFirstLetterOfState());
            }
            if (!(checkWin.toString().equals("RRRR") || checkWin.toString().equals("YYYY"))) {
                checkWin = new StringBuilder();
                y1 = 0;
                z1 = 3;

                for (int x1 = 0; x1 < 4; x1++) {
                    checkWin.append(board[x1][y1++][z1--].getFirstLetterOfState());
                }
            }
        }

        return getStateFromString(String.valueOf(checkWin));
    }

    /**
     * Checks if win condition is fulfilled(marked with * or ? )
     * Placed Piece (110)
     * |---|0.Layer|---|		|---|1.Layer|---|		|---|2.Layer|---|		|---|3.Layer|---|
     * | * |100|200|300|		|001| * |201|301|		|002|102| * |302|		|003|103|203| * |
     * |---|---|---|---|		|---|---|---|---|		|---|---|---|---|		|---|---|---|---|
     * |010|110|210| ? |		|011|111| ? |311|		|012| ? |212|312|		| ? |113|213|313|
     *
     * @param y
     * @return boolean that checks if there are four of the same kind on the Axis ZX in Position Y
     */
    private State checkZXDiagonalOnY(Integer y) {
        StringBuilder checkWin = new StringBuilder();
        int z1 = 0;


        for (int x1 = 0; x1 < 4; x1++) {
            checkWin.append(board[x1][y][z1++].getFirstLetterOfState());
        }

        if (checkWin.toString().equals("RRRR")) return State.RED;
        else if (checkWin.toString().equals("YYYY")) return State.YELLOW;
        else {
            checkWin = new StringBuilder();
            z1 = 0;

            for (int x1 = 3; x1 >= 0; x1--) {
                checkWin.append(board[x1][y][z1++].getFirstLetterOfState());
            }
        }
        return getStateFromString(String.valueOf(checkWin));
    }

    private State getStateFromString(String checkWin) {
        if (checkWin.toString().equals("RRRR")) return State.RED;
        else if (checkWin.toString().equals("YYYY")) return State.YELLOW;
        else return State.BLANK;
    }

    /**
     * Checks if win condition is fulfilled(marked with * or ?)
     * Placed Piece (110)
     * |---|0.Layer|---|		|---|1.Layer|---|		|---|2.Layer|---|		|---|3.Layer|---|
     * |000| * |200|300|		|001|101|201|301|		|002|102|202|302|		|003|103|203| ? |
     * |---|---|---|---|		|---|---|---|---|		|---|---|---|---|		|---|---|---|---|
     * |010|110|210|310|		|011| * |211|311|		|012|112|212| ? |		|013|113|213|313|
     * |---|---|---|---|		|---|---|---|---|		|---|---|---|---|		|---|---|---|---|
     * |020|120|220|320|		|021|121|221| ? |		|022| * |222|322|		|023|123|223|323|
     * |---|---|---|---|		|---|---|---|---|		|---|---|---|---|		|---|---|---|---|
     * |030|130|230| ? |		|031|131|231|331|		|032|132|232|332|		|033| * |233|333|
     * |---|---|---|---|		|---|---|---|---|		|---|---|---|---|		|---|---|---|---|
     *
     * @param x
     * @return boolean that checks if there are four of the same kind on the Axis ZX in Position Y
     */
    private State checkZYDiagonalOnX(Integer x) {
        StringBuilder checkWin = new StringBuilder();
        int z1 = 0;


        for (int y1 = 0; y1 < 4; y1++) {
            checkWin.append(board[x][y1][z1++].getFirstLetterOfState());
        }

        if (checkWin.toString().equals("RRRR")) return State.RED;
        else if (checkWin.toString().equals("YYYY")) return State.YELLOW;
        else {
            checkWin = new StringBuilder();
            z1 = 0;

            for (int y1 = 3; y1 >= 0; y1--) {
                checkWin.append(board[x][y1][z1++].getFirstLetterOfState());
            }
        }
        return getStateFromString(String.valueOf(checkWin));
    }

    /**
     * Checks if win condition is fulfilled(marked with *)
     * Placed Piece (000)
     * |---|0.Layer|---|		|---|1.Layer|---|		|---|2.Layer|---|		|---|3.Layer|---|
     * | * |100|200|300|		| * |101|201|301|		| * |102|202|302|		| * |103|203|303|
     *
     * @param x
     * @param y
     * @return boolean that checks if there are four of the same kind on the Axis Z in Position XY
     */
    private State checkZAxisOnXY(Integer x, Integer y) {
        String checkPieces = "";

        //Check VerticalDepth on Position xy
        for (int z = 0; z < 4; z++) {
            checkPieces += board[x][y][z].getFirstLetterOfState();
        }

        return getStateFromString(checkPieces);
    }


    /**
     * Checks if win condition is fulfilled(marked with *)
     * Placed Piece (110)
     * |---|0.Layer|---|
     * |000| * |200|300|
     * |---|---|---|---|
     * |010| * |210|310|
     * |---|---|---|---|
     * |020| * |220|320|
     * |---|---|---|---|
     * |030| * |230|330|
     * |---|---|---|---|
     *
     * @param x
     * @param z
     * @return boolean that checks if there are four of the same kind on the Axis Y in Position X
     */
    private State checkYAxisOnX(Integer x, Integer z) {
        String checkPieces = "";

        //Check Vertical on Position x
        for (int y = 0; y < 4; y++) {
            checkPieces += board[x][y][z].getFirstLetterOfState();
        }

        return getStateFromString(checkPieces);
    }


    /**
     * Checks if win condition is fulfilled(marked with *)
     * Placed Piece (110)
     * |---|0.Layer|---|
     * |000|100|200|300|
     * |---|---|---|---|
     * | * | * | * | * |
     * |---|---|---|---|
     * |020|120|220|320|
     * |---|---|---|---|
     * |030|130|230|330|
     * |---|---|---|---|
     *
     * @param y
     * @param z
     * @return boolean that checks if there are four of the same kind on the Axis X in Position Y
     */
    private State checkXAxisOnY(Integer y, Integer z) {
        String checkPieces = "";

        //Check Vertical on Position x
        for (int x = 0; x < 4; x++) {
            checkPieces += board[x][y][z].getFirstLetterOfState();
        }

        return getStateFromString(checkPieces);
    }


    /**
     * Checks if win condition is fulfilled(marked with * or ?)
     * Placed Piece (110)
     * |---|0.Layer|---|
     * | * |100|200| ? |
     * |---|---|---|---|
     * |010| * | ? |310|
     * |---|---|---|---|
     * |020| ? | * |320|
     * |---|---|---|---|
     * | ? |130|230| * |
     * |---|---|---|---|
     *
     * @param x
     * @param y
     * @param z
     * @return boolean that checks if the win condition is fulfilled on a diagonal
     */
    private State checkXYDiagonal(Integer x, Integer y, Integer z) {
        boolean hasToBeReversed;

        // This argument checks if Position is at the corners (x == 0 || x == 3 && y == 0 || y == 3)
        // (x == 1 && y == 1), (x == 1 && y == 2),(x == 2 && y == 1), (x == 2 && y == 2)),
        // Checks if position is this field

        if ((
                ((x == 0 && y == 0) || (x == 3 && y == 3))
                        || (x == 1 && y == 1)
                        || (x == 2 && y == 2)
        )) {
            hasToBeReversed = false;
        } else if (
                ((x == 0 && y == 3) || (x == 3 && y == 0))
                        || (x == 1 && y == 2)
                        || (x == 2 && y == 1)
        ) {
            hasToBeReversed = true;
        } else return State.BLANK;


        String checkWin = "";
        int y1 = 0;

        if (hasToBeReversed) {
            for (int x1 = 3; x1 >= 0; x1--) {
//                System.out.println("X: " + x1 + " Y: " + y1);
                checkWin += board[x1][y1++][z].getFirstLetterOfState();
            }
        } else {
            for (int x1 = 0; x1 < 4; x1++) {
//                System.out.println("X: " + x1 + " Y: " + y1);
                checkWin += board[x1][y1++][z].getFirstLetterOfState();
            }
        }


        return getStateFromString(checkWin);

    }


    public static void main(String[] args) {
        BackendLogic service = new BackendLogic();
        service.launcher();


    }

}
