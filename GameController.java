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
    //private String filename;
    private int portNumber;
    //private int boardSize;
    private int numberOfStars;
    private String role;
    private MapParser parser;
    private Socket gameSocket;
    private PrintWriter outputStream;
    private BufferedReader inputStream;

    private Spoiler spoilerAI;
    private Choreographer choreographerAI;

    GameController(String filename, int portNumber, int boardSize, int numberOfStars, String role) throws IOException {
        this.portNumber = portNumber;
        //this.filename = filename;
        //this.boardSize = boardSize;
        this.numberOfStars = numberOfStars;
        this.role = role;
        parser = new MapParser(filename, boardSize);
        connectToSocket();
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
      char[] incomingString;
        if(role.equals("S")){
          while (true) {
            incomingString = new char[4096];
            inputStream.read(incomingString, 0, 4096);
            if (incomingString[0] == '^') {
              System.out.println("We are the spoiler");
              spoilerAI = new Spoiler(parser.map, numberOfStars);
              writeToSocket(spoilerAI.getStars());
            } else if (incomingString[0] =='$') {
              break;
            }
          }
        } else {
            while(true) {
                incomingString = new char[4096];
                inputStream.read(incomingString, 0, 4096);
                //System.out.println("We are the choreographer");
                if (incomingString[0] == '$') {
                    return;
                }
                //System.out.println("Received:" + String.valueOf(incomingString));
                //System.out.println(incomingString.length);
                if(String.valueOf(incomingString).trim().length() >= 2) {
                    parser.addStars(new String(incomingString));
                    Helper.printMap(parser.map);
                    choreographerAI = new Choreographer(parser.map);
                }
                if(String.valueOf(incomingString).contains("#")) {
                    writeToSocket(choreographerAI.getMoves());
                }
            }
        }
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
