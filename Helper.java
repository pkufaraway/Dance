import java.util.List;

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

    public static void printPin(boolean[][] pin){
        int size = pin.length;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                if(pin[i][j]){
                    System.out.printf("P ");
                } else {
                    System.out.printf("N ");
                }
            }
            System.out.println();
        }
    }
    public static void copyMap(char[][] map1, char[][] map2){
        int size = map1.length;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                map2[i][j] = map1[i][j];
            }
        }
    }

    public static void refreshMap(List<Pair> moves, char[][] map){
        int size = map.length;
        boolean[][] newPosition = new boolean[size][size];
        for(Pair p : moves) {
            Position end = p.end;
            map[end.x][end.y] = p.color;
            newPosition[end.x][end.y] = true;
        }
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(!newPosition[i][j] && map[i][j] != 'S'){
                    map[i][j] = 0;
                }
            }
        }
    }
}
