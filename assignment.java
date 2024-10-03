package Yanamadala_M_p2;
import java.util.Scanner;


public class assignment {
    private static String[][] board;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Please enter size of board as a power of 2 (0 to quit): ");
                int boardSize = scanner.nextInt();
    
                if (boardSize == 0) {
                    break;
                }
    
                if (!isPowerOfTwo(boardSize) || boardSize == 1) {
                    System.out.println("The board size should be a power of 2.");
                    continue;
                }
    
                System.out.print("Please enter coordinates of missing square (separate by a space): ");
                int x_missing = scanner.nextInt();
                int y_missing = scanner.nextInt();
    
                board = new String[boardSize][boardSize];
                for (int i = 0; i < boardSize; i++) {
                    for (int j = 0; j < boardSize; j++) {
                        board[i][j] = "MS";
                    }
                }
    
                tromino(0, 0, x_missing, y_missing, boardSize);
                printBoard(boardSize);
            }
        }
    }
    
    private static boolean isPowerOfTwo(int number) {
        return (number & (number - 1)) == 0;
    }

    private static void tromino(int x_board, int y_board, int x_missing, int y_missing, int board_size) {
        if (board_size == 2) {
            if (x_missing == x_board && y_missing == y_board) {
                board[x_board][y_board + 1] = "UR";
                board[x_board + 1][y_board] = "UR";
                board[x_board + 1][y_board + 1] = "UR";
            } else if (x_missing == x_board && y_missing == y_board + 1) {
                board[x_board][y_board] = "LR";
                board[x_board + 1][y_board] = "LR";
                board[x_board + 1][y_board + 1] = "LR";
            } else if (x_missing == x_board + 1 && y_missing == y_board) {
                board[x_board][y_board] = "UL";
                board[x_board][y_board + 1] = "UL";
                board[x_board + 1][y_board + 1] = "UL";
            } else if (x_missing == x_board + 1 && y_missing == y_board + 1) {
                board[x_board][y_board] = "LL";
                board[x_board + 1][y_board] = "LL";
                board[x_board][y_board + 1] = "LL";
            }
            return;
        }

        int half_size = board_size / 2;
        int center_x = x_board + half_size - 1;
        int center_y = y_board + half_size - 1;

        setCenterTromino(center_x, center_y, x_missing, y_missing);

        int[] missing_positions = getMissingPositions(x_board, y_board, x_missing, y_missing, half_size, center_x, center_y);

        tromino(x_board, y_board, missing_positions[0], missing_positions[1], half_size); 
        tromino(x_board, y_board + half_size, missing_positions[2], missing_positions[3], half_size);
        tromino(x_board + half_size, y_board, missing_positions[4], missing_positions[5], half_size); 
        tromino(x_board + half_size, y_board + half_size, missing_positions[6], missing_positions[7], half_size); 
    }

    private static void setCenterTromino(int center_x, int center_y, int x_missing, int y_missing) {
        fillTrominoType(center_x, center_y, x_missing, y_missing);
    }

    private static int[] getMissingPositions(int x_board, int y_board, int x_missing, int y_missing, int half_size, int center_x, int center_y) {
        int[] missing_positions = {
            center_x, center_y, 
            center_x, center_y + 1, 
            center_x + 1, center_y, 
            center_x + 1, center_y + 1 
        };
    
        if (x_missing < x_board + half_size) {
            if (y_missing < y_board + half_size) {
                missing_positions[0] = x_missing;
                missing_positions[1] = y_missing;
                missing_positions[2] = center_x;
                missing_positions[3] = center_y + 1;
                missing_positions[4] = center_x + 1;
                missing_positions[5] = center_y;
                missing_positions[6] = center_x + 1;
                missing_positions[7] = center_y + 1;
            } else {
                missing_positions[2] = x_missing;
                missing_positions[3] = y_missing;
                missing_positions[0] = center_x;
                missing_positions[1] = center_y;
                missing_positions[4] = center_x + 1;
                missing_positions[5] = center_y;
                missing_positions[6] = center_x + 1;
                missing_positions[7] = center_y + 1;
            }
        } else {
            if (y_missing < y_board + half_size) {
                missing_positions[4] = x_missing;
                missing_positions[5] = y_missing;
                missing_positions[0] = center_x;
                missing_positions[1] = center_y;
                missing_positions[2] = center_x;
                missing_positions[3] = center_y + 1;
                missing_positions[6] = center_x + 1;
                missing_positions[7] = center_y + 1;
            } else {
                missing_positions[6] = x_missing;
                missing_positions[7] = y_missing;
                missing_positions[0] = center_x;
                missing_positions[1] = center_y;
                missing_positions[2] = center_x;
                missing_positions[3] = center_y + 1;
                missing_positions[4] = center_x + 1;
                missing_positions[5] = center_y;
            }
        }
    
        return missing_positions;
    }

    private static int determineQuadrant(int x, int y, int center_x, int center_y) {
        if (x <= center_x) {
            return (y <= center_y) ? 0 : 1;
        } else {
            return (y <= center_y) ? 2 : 3;
        }
    }


    private static void printBoard(int boardSize) {
        for (int i = boardSize - 1; i >= 0; i--) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(board[j][i] + " ");
            }
            System.out.println();
        }
    }


    private static void fillTrominoType(int center_x, int center_y, int x_missing, int y_missing) {
        int quadrant = determineQuadrant(x_missing, y_missing, center_x, center_y);
        String[] types = {"UR", "LR", "UL", "LL"};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int sub_x = center_x + i;
                int sub_y = center_y + j;
                if (sub_x != x_missing || sub_y != y_missing) {
                    board[sub_x][sub_y] = types[quadrant];
                }
            }
        }
    }
}
