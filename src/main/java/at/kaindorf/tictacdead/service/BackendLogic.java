package at.kaindorf.tictacdead.service;

import at.kaindorf.tictacdead.pojos.Position;
import at.kaindorf.tictacdead.pojos.State;

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

        if ((x < 0 || x > 3) || (y < 0 || y > 3)  || (z < 0 || z > 3)) {
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



    public void launcher() {

        State state;
        Scanner scanner = new Scanner(System.in);
        Integer coordinates;
        String tmp;


        // Initialize board with printout
        fillBoardWithBlankPositions();  /*ToDo: add to Constructor*/
        printBoard();

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

            if (checkWin(coordinates)) {
                System.out.println(GREEN_BOLD + GREEN_UNDERLINED + state + " won" + ANSI_RESET);
                return;
            }
        }
        System.out.println("The game ended in a draw");
    }

    /**
     * Method that determines after a placement if the affected rows fulfill the win condition
     * It calls 2 other methods. (LayerCheckWin, MultiLayerCheckWin)
     *
     * @param coordinates
     * @return boolean to check if there is a winner
     */
    public boolean checkWin(Integer coordinates) {
        Integer x = coordinates / 100;
        Integer y = (coordinates / 10) % 10;
        Integer z = coordinates % 10;

        System.out.println(x);
        System.out.println(y);
        System.out.println(z);

        return layerWinCheck(x, y, z) || multiLayerWinCheck(x, y);
    }

    private boolean multiLayerWinCheck(Integer x, Integer y) {
        return checkZAxisOnXY(x, y) || checkZYDiagonalOnX(x) || checkZXDiagonalOnY(y) || checkZDiagonalWithXY(x, y);
    }

    private boolean checkZDiagonalWithXY(Integer x, Integer y) {

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
        } else return false;



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

        return checkWin.toString().equals("RRRR") || checkWin.toString().equals("YYYY");
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
    private boolean checkZXDiagonalOnY(Integer y) {
        StringBuilder checkWin = new StringBuilder();
        int z1 = 0;


        for (int x1 = 0; x1 < 4; x1++) {
            checkWin.append(board[x1][y][z1++].getFirstLetterOfState());
        }

        if (checkWin.toString().equals("RRRR") || checkWin.toString().equals("YYYY")) {
            return true;
        } else {
            checkWin = new StringBuilder();
            z1 = 0;

            for (int x1 = 3; x1 >= 0; x1--) {
                checkWin.append(board[x1][y][z1++].getFirstLetterOfState());
            }
        }
        return checkWin.toString().equals("RRRR") || checkWin.toString().equals("YYYY");
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
    private boolean checkZYDiagonalOnX(Integer x) {
        StringBuilder checkWin = new StringBuilder();
        int z1 = 0;


        for (int y1 = 0; y1 < 4; y1++) {
            checkWin.append(board[x][y1][z1++].getFirstLetterOfState());
        }

        if (checkWin.toString().equals("RRRR") || checkWin.toString().equals("YYYY")) {
            return true;
        } else {
            checkWin = new StringBuilder();
            z1 = 0;

            for (int y1 = 3; y1 >= 0; y1--) {
                checkWin.append(board[x][y1][z1++].getFirstLetterOfState());
            }
        }
        return checkWin.toString().equals("RRRR") || checkWin.toString().equals("YYYY");
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
    private boolean checkZAxisOnXY(Integer x, Integer y) {
        String checkPieces = "";

        //Check VerticalDepth on Position xy
        for (int z = 0; z < 4; z++) {
            checkPieces += board[x][y][z].getFirstLetterOfState();
        }

        return checkPieces.equals("RRRR") || checkPieces.equals("YYYY");
    }

    private boolean layerWinCheck(Integer x, Integer y, Integer z) {
        return checkXAxisOnY(y, z) || checkYAxisOnX(x, z) || checkXYDiagonal(x, y, z);
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
    private boolean checkYAxisOnX(Integer x, Integer z) {
        String checkPieces = "";

        //Check Vertical on Position x
        for (int y = 0; y < 4; y++) {
            checkPieces += board[x][y][z].getFirstLetterOfState();
        }

        return checkPieces.equals("RRRR") || checkPieces.equals("YYYY");
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
    private boolean checkXAxisOnY(Integer y, Integer z) {
        String checkPieces = "";

        //Check Vertical on Position x
        for (int x = 0; x < 4; x++) {
            checkPieces += board[x][y][z].getFirstLetterOfState();
        }

        return checkPieces.equals("RRRR") || checkPieces.equals("YYYY");
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
    private boolean checkXYDiagonal(Integer x, Integer y, Integer z) {
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
        } else return false;


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




        return checkWin.equals("RRRR") || checkWin.equals("YYYY");

    }


    public static void main(String[] args) {



        BackendLogic service = new BackendLogic();
        service.launcher();


    }

}
