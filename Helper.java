/**
 * Created by yaoyuanliu on 10/30/16.
 */
public class Helper {
    public static void printMap(char[][] map){
        int size = map.length;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                if(map[i][j] == 0){
                    System.out.printf("* ");
                } else {
                    System.out.printf("%c ", map[i][j]);
                }
            }
            System.out.println();
        }
    }
}
