import java.net.*;
import java.io.*;
import java.util.Random;

public class ServerThread extends Thread {
    private Socket toClient;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private PrintWriter out;
    private BufferedReader in;
    private Random randomMoves;
    private char[][] board;
    private int row;
    private int col;

    public ServerThread(Socket newSocket) {
        toClient = newSocket;
        randomMoves = new Random();

        try {
            inStream = new DataInputStream(toClient.getInputStream());
            outStream = new DataOutputStream(toClient.getOutputStream());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        out = new PrintWriter(outStream, true);
        in = new BufferedReader(new InputStreamReader(inStream));
        board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        row = -1;
        col = -1;
    }

    public void run() {
        int counter = 0;
        String response = "";
        boolean gameOver = false;
        boolean turn = false;
        int randomInt = randomMoves.nextInt(2) + 1;

        if (randomInt == 2) {
            turn = true;
            out.println("NONE");
        }
        else {
            turn = false;
        }

        while (!gameOver) {
            if (turn) {
                try {
                    response = in.readLine();
                } catch (IOException ex) {
                    System.out.println("Read error on socket in server thread.");
                }

                String[] data = response.split("\\s+");
                row = Integer.parseInt(data[1]);
                col = Integer.parseInt(data[2]);
                board[row][col] = 'O';
                counter++;

                if (checkWin() || counter == 9) {
                    gameOver = true;

                    if (checkWin()) {
                        out.println("USER:MOVE -1 -1 WIN");
                    } else {
                        out.println("USER:MOVE -1 -1 TIE");
                    }
                }
                else {
                    out.println("USER:MOVE " + row + " " + col);
                }

                turn = false;
            }

            else {
                makeMove();
                board[row][col] = 'X';
                counter++;

                if (checkWin() || counter == 9) {
                    gameOver = true;
                    if (checkWin()) {
                        out.println("SERVER:MOVE " + row + " " + col + " LOSS");
                    }
                    else {
                        out.println("SERVER:MOVE " + row + " " + col + " TIE");
                    }
                }
                else {
                    out.println("SERVER:MOVE " + row + " " + col);
                }

                turn = true;
            }
        }
    }

    void makeMove() {
        boolean exit = false;

        while (!exit) {
            int randomRow = randomMoves.nextInt(3);
            int randomCol = randomMoves.nextInt(3);

            if (board[randomRow][randomCol] == ' ') {
                row = randomRow;
                col = randomCol;
                exit = true;
            }
        }
    }

    boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' ') {
                return true;
            }

            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != ' ') {
                return true;
            }
        }

        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') {
            return true;
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ') {
            return true;
        }

        return false;
    }

    void printBoard() {
        out.println(board[0][0] + " | " + board[0][1] + " | " + board[0][2]);
        out.println("-----------");
        out.println(board[1][0] + " | " + board[1][1] + " | " + board[1][2]);
        out.println("-----------");
        out.println(board[2][0] + " | " + board[2][1] + " | " + board[2][2]);
        out.println("\n\n");
    }
}
