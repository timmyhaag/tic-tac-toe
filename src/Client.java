import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static String hostName = "localhost";
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static PrintWriter out;
    private static BufferedReader in;
    private static Socket toServer;
    private static char[][] board;
    private static int row;
    private static int col;

    public static void main(String[] args) {
        board = new char[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }

        row = -1;
        col = -1;

        try {
            toServer = new Socket("localhost", 7788);
            in = new BufferedReader(new InputStreamReader(toServer.getInputStream()));
            out = new PrintWriter(toServer.getOutputStream(), true);
            playGame(in, out);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void  playGame(BufferedReader in, PrintWriter out) throws IOException {
        Scanner input = new Scanner(System.in);
        String response;
        String winMessage = "";
        boolean turn = false;
        boolean gameOver = false;
        System.out.println("Let's play Tic-tac-toe!\n");

        while (!gameOver) {
            if (turn) {
                do {
                    System.out.print("ENTER YOUR MOVE");
                    System.out.print("\nEnter Row: ");
                    row = input.nextInt();
                    System.out.print("Enter Column: ");
                    col = input.nextInt();
                    System.out.print("\n");
                } while ( row < 0 || row > 2 || col > 2 || col < 0 || board[row][col] != ' ' );

                board[row][col] = 'O';
                out.println("MOVE " + row + " " + col);

                response = in.readLine();
                System.out.println(response);

                if (response.equals("USER:MOVE -1 -1 WIN")) {
                    winMessage = "Congratulations!!! You WON the game!";
                    printBoard();
                }
                if (response.equals("USER:MOVE -1 -1 TIE")) {
                    winMessage = "\nThe game was a TIE!";
                    printBoard();
                }
            }

            else {
                response = in.readLine();
                System.out.println(response);

                if (!response.equals("NONE")) {
                    String[] args = response.split("\\s+");
                    if (args.length  > 3 ) {
                        row = Integer.parseInt(args[1]);
                        col = Integer.parseInt(args[2]);
                        if ((!args[3].equals("WIN")) && (row != -1)) {
                            board[row][col] = 'X';
                            printBoard();
                        }

                        switch (args[3] ) {
                            case "WIN" :
                                System.out.println("\n\nCongratulations!!! You WON the game!");
                                break;
                            case "TIE" :
                                System.out.println("\nThe game was a TIE!");
                                break;
                            case "LOSS" :
                                System.out.println("\nSORRY! You LOST the game!");
                                break;
                        }
                        gameOver = true;
                    }

                    else {
                        row = Integer.parseInt(args[1]);
                        col = Integer.parseInt(args[2]);
                        board[row][col] = 'X';
                    }
                }

                else {
                    System.out.println("\nYOU MOVE FIRST\n");
                }
            }

            if ((winMessage.equals("Congratulations!!! You WON the game!")) || (winMessage.equals("\nThe game was a TIE!"))) {
                System.out.println(winMessage);
            }

            printBoard();

            if ((winMessage.equals("Congratulations!!! You WON the game!")) || (winMessage.equals("\nThe game was a TIE!"))) {
                System.out.println("\nHere is the final game board");
                printBoard();
                System.exit(0);
            }
            turn = !turn;
        }

        System.out.println("\nHere is the final game board");
        printBoard();
    }

    static void printBoard() {
        System.out.println(board[0][0] + " | " + board[0][1] + " | " +board[0][2]);
        System.out.println("----------");
        System.out.println(board[1][0] + " | " + board[1][1] + " | " + board[1][2]);
        System.out.println("----------");
        System.out.println(board[2][0] + " | " + board[2][1] + " | " + board[2][2]);
        System.out.println("\n");
    }
}