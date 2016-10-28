import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class MapParser {

    public char[][] map;

    public void addStars(String stars){
        String[] starList = stars.split(" ");
        for(int i = 0; i < starList.length; i+=2){
            int x = Integer.valueOf(starList[i]);
            int y = Integer.valueOf(starList[i]);
            map[x][y] = 'S';
        }
    }

    public MapParser(String filename, int boardSize){
        String line;
        map = new char[boardSize][boardSize];
        char currentColor = 0;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(filename);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if(line.trim().length() == 0){
                    continue;
                }
                if(line.charAt(0) == 'R'){
                    currentColor = 'R';
                } else if(line.charAt(0) == 'B') {
                    currentColor = 'B';
                } else {
                    String[] splitStrings = line.trim().split(" ");
                    int x = Integer.valueOf(splitStrings[0]);
                    int y = Integer.valueOf(splitStrings[1]);
                    map[x][y] = currentColor;
                }
            }
            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            filename + "'");
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
