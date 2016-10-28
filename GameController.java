import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/*** Class that connects to a socket (5000 if the socket number is not defined
 * by the user), and reads the entire graph from the socket. You can get the
 * elements of the game from this class via getter methods. You can write your 
 * move to the server and the server will call the AI with the newest data
 * from the server.
 *
 * @author William Brantley
 *
 */
class GameController {
    private String filename;
    private int portNumber;
    private int boardSize;
    private int numberOfStars;
    private MapParser parser;
    private Socket gameSocket;
    private PrintWriter outputStream;
    private BufferedReader inputStream;

    private Spoiler spolierAI;
    private Choreographer choreographerAI;

    GameController(String filename, int portNumber, int boardSize, int numberOfStars) throws IOException {
        this.portNumber = portNumber;
        this.filename = filename;
        this.boardSize = boardSize;
        this.numberOfStars = numberOfStars;
        parser = new MapParser(filename, boardSize);
        connectToSocket();
        writeToSocket("WA\n");
        listenForMoves();
        endGame();
    }

    private void connectToSocket() {
        try {
            gameSocket = new Socket(InetAddress.getLocalHost(), this.portNumber);
            outputStream = new PrintWriter(gameSocket.getOutputStream(), true);
            inputStream = new BufferedReader(
                    new InputStreamReader(gameSocket.getInputStream()));
        } catch (Exception notHandled) {
            notHandled.printStackTrace();
        }
    }

    private void listenForMoves() throws IOException {
        String incomingString;
        while (true) {
            incomingString = inputStream.readLine();
            //System.out.println(incomingString);

            if(incomingString.trim().equals("^")){
                System.out.println("We are the spoiler");
                spolierAI = new Spoiler(parser.map, numberOfStars);
                writeToSocket(spolierAI.getStars());
            } else {
                System.out.println("We are the choreographer");
                choreographerAI = new Choreographer(parser.map);
                writeToSocket(choreographerAI.getMoves());
            }
        }
        //endGame();
    }

    private void writeToSocket(String moveToMake) {
        outputStream.write(moveToMake);
        outputStream.flush();
    }

    private void endGame() throws IOException {
        outputStream.close();
        inputStream.close();
        gameSocket.close();
    }

}
