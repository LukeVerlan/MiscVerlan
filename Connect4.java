import java.util.*;

// Plays Connect 4 
public class Connect4 {

    public static void main(String[] args){
        Scanner user = new Scanner(System.in);
        int rows = 6;
        int columns = 7; 
        String [][] board = new String[rows][columns];
        int turn = 0; 
        int winState = -1;
        FillBoard(board, rows, columns);

        while(!CheckTie(board)) {

            turn++; // used to track whos turn it is
            PlayGame(board, turn, rows, columns, user);
    
            if(CheckWin(board,rows) == 1){
                winState = 1; 
                break;
            }

            if(CheckWin(board,rows) == 2){
                winState = 2; 
                break; 
            }
        } 

        PrintBoard(board, rows, columns);

        if(winState == -1){
            System.out.println("Tie :D");
        }

        if(winState == 1){
            System.out.println("Player X WINS!");
        }

        if(winState == 2){
            System.out.println("Player O WiNS!");
        }
    }
    
    // Checks to see if board is full 
    // When the board fills up, tie occurs
    public static boolean CheckTie (String[][] board) {
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 7; j++){
                if(board[i][j] == "-"){
                    return false; 
                }
            }
        }

        return true; 
    }

    // Fills board with "-"
    public static void FillBoard (String[][] board, int rows, int columns) {
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                board[i][j] = "-";
            }
        }
    }   

    // Plays the game 
    public static void PlayGame (String[][] board, int turn, int rows, int columns, Scanner user){
        String token = "E";

        if(turn % 2 == 1) {
            System.out.println("Player X Turn");  
            token = "X";        
        } else {
            System.out.println("Player O Turn");
            token = "O";
        }

        System.out.println();
        System.out.println("Place your token");

        PrintBoard(board, rows, columns);
        System.out.println();

        int userColumn = -1; 

         while(userColumn > 7 || userColumn < 1) {
            System.out.println("Which Column would you like to place your token? (1-7)");
            userColumn = user.nextInt(); 
            if(userColumn > 7 || userColumn < 1){
                System.out.println("Number outside of column range");
                System.out.println();
            }
        }

        userColumn--;

        DropToken(board, userColumn, token, rows, columns, user, turn);

    }   

    // Creates dropping token into slot effect 
    // Calls play game in event of dropping in a full column 
    public static void DropToken (String[][] board, int userColumn, String token, int rows, 
        int columns, Scanner user, int turn) {

        String[] column = getColumn(board, userColumn, rows);
        String index = "-";
        int counter = 0; 

        while(index == "-") {
            index = column[counter];
            if(index != "-" && counter == 0){
                System.out.println("Column full, Try again");
                PlayGame(board, turn, rows, columns, user);
            } else if (index != "-" && counter > 0){
                column[counter - 1] = token; 
                break;
             
            } else if(counter == rows - 1){
                column[counter] = token;
                break; 
            }
        counter++; 
        }

        for(int i = 0; i < rows; i++){

            board[i][userColumn]= column[i];
        }
    }

    // Checks to see if there is a win on the board in either of the columns, rows, or diagonals
    public static int CheckWin(String[][] board, int rows) {

        int upper = 4; // Used to shift the win state being checked 
        int lower = 0;

        for(int i = 0; i < 6; i++) {
        
            for(int j = 0; j < 4; j++) {
                String[] rowState = new String[4];
                int counterR = 0; 
                for(int k = lower; k < upper; k++) {
                    rowState[counterR] = board[i][k];
                    counterR++; 
                }

                if(CheckForContinuity(rowState)){
                    if(rowState[0] == "X"){
                        return 1; 
                    }
                
                    if(rowState[0] == "O"){
                        return 2; 
                    }  
                }
            lower++;
            upper++;
            }
        lower = 0; 
        upper = 4; 
        }

        for(int i = 0; i < 7; i++) {
        
            String[] fullColumnState = getColumn(board, i, rows);

            for(int j = 0; j < 3; j++) {

                String[] columnState = new String[4];
                int counterC = 0;
                for(int k = lower; k < upper; k++){
                    columnState[counterC] = fullColumnState[k];
                    counterC++;
                }

            
                if(CheckForContinuity(columnState)){
                    if(columnState[0] == "X"){
                        return 1; 
                    }
                
                    if(columnState[0] == "O"){
                        return 2; 
                    }  
                }

            lower++;
            upper++;
            }

        lower = 0; 
        upper = 4; 
        }

        char right = 'r';
        char left = 'l';

        int[][] RstartPoints =  
        {{0,3}, {0,4}, {0,5},
        {1,3}, {1,4}, {1,5},
        {2,3}, {2,4}, {2,5},
        {3,3}, {3,4}, {3,5}};
        int[][] LstartPoints = 
        {{6,3}, {6,4}, {6,5},
        {5,3}, {5,4}, {5,5},
        {4,3}, {4,4}, {4,5},
        {3,3}, {3,4}, {3,5}};

    

        for(int i = 0; i < 12; i++){

            String[] rightDiag = getDiagonalState(board, RstartPoints[i], right);
            String[] leftDiag = getDiagonalState(board, LstartPoints[i], left );

            if(CheckForContinuity(rightDiag)){
                if(rightDiag[0] == "X"){
                    return 1; 
                }
                
                if(rightDiag[0] == "O"){
                    return 2; 
                }  
            }

             if(CheckForContinuity(leftDiag)){
                if(leftDiag[0] == "X"){
                    return 1; 
                }
                
                if(leftDiag[0] == "O"){
                    return 2; 
                }                 
            }
        }   
        return -1; 
    }

    // Checks to see if all elements in a win state are the same 
    public static boolean CheckForContinuity (String[] state) {

        boolean[] continuity = new boolean[4];

        if(state[0] != "-"){
            for(int i = 0; i < 3; i++){
                if(state[i] == state [i+1]){
                    continuity[i] = true;
                }
            } 

            if(state[2] == state[3]){
                continuity[3] = true; 
            }

            if(continuity[0] && continuity[1] && continuity[2] && continuity[3]) {
                return true; 
            }   
        }
        return false; 
    }

    // Prints the current playing board into the console 
    public static void PrintBoard (String[][] board, int rows, int columns){

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                System.out.print(" " + board[i][j] + " ");
            }
            System.out.println(); 
        }
    }

    // Gets the requested column from the board 
    public static String[] getColumn(String[][] board, int userColumn, int rows) {
        String[] column = new String[rows];
        for(int i = 0; i < rows; i++){
            column[i] = board[i][userColumn];
        }    
        return column;
    } 

    // Gets a requested diagonal using a start point and a direction to take the diagonal in
    public static String[] getDiagonalState(String[][] board, int[] start, char direction) {

        String[] diag = new String[4];

        int xlocation = start[0];
        int ylocation = start[1];

        if(direction == 'r') {
          
            for(int i = 0; i < 4; i++){ 
                diag[i] = board[ylocation][xlocation];
                ylocation--;
                xlocation++;
            }

            return diag; 
        }

        if(direction == 'l') {
        
            for(int i = 0; i < 4; i++){
                diag[i] = board[ylocation][xlocation];
                ylocation--;
                xlocation--;
            }
            return diag; 
        }

        return null; 
    }
}   