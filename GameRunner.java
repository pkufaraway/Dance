import java.io.IOException;
import java.io.InputStream;

public class GameRunner {

    public static void main(String[] args) throws IOException {
        GameController gameController = null;
        String filename = args[0];
        int portNumber = Integer.valueOf(args[1]);
        int boardSize = Integer.valueOf(args[2]);
        int numberOfStars = Integer.valueOf(args[3]);
        String role = args[4];
        if(args.length < 5){
            System.out.println("python test_spoiler.py <string:dancer_locations> <int:port_number> <int:board_size> <int:number_of_stars>");
        }

        gameController = new GameController(filename, portNumber, boardSize, numberOfStars, role);
    }

}
