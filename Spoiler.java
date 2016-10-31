public class Spoiler {
    char[][] map;

    int numberOfStars;
    int size;

    public Spoiler(char[][] map, int numberOfStars){
        this.numberOfStars = numberOfStars;
        System.out.println(numberOfStars);
        this.map = map;
        size = map.length;
        Helper.printMap(map);
    }

    public static double distance(int x1,int y1, int x2, int y2){
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    /**
     * Detect whether a star position x,y is valid, no other stars within 4
     * @param x x coordinate
     * @param y y coordinate
     * @return true for valid, false for not valid
     */
    private boolean isValid(int x, int y){
        if(map[x][y] != 0){
            return false;
        }
        for(int i = x - 4; i <= x + 4; i++){
            for(int j = y - 4; j <= y + 4; j++){
                if(distance(i, j, x, y) < 4 &&
                    i >= 0 && i < size && j >= 0 && j < size
                    && map[i][j] == 'S'){
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * find all the star position and return the string;
     * @return the numberOfStars pairs string for all stars' coordinates.
     */
    public String getStars(){
        int n = map.length;
        int number = numberOfStars;
        StringBuilder answer = new StringBuilder();
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(isValid(i, j)){
                    //System.out.printf("Add new %d %d\n", i, j);
                    answer.append(i);
                    answer.append(' ');
                    answer.append(j);
                    answer.append(' ');
                    map[i][j] = 'S';
                    number--;
                    if(number == 0){
                        answer.deleteCharAt(answer.length() - 1);
                        answer.append('\n');
                        System.out.println(answer);
                        return answer.toString();
                    }
                }
            }
        }
        return answer.toString();
    }

    /*Test
    public static void main(String[] args){
        int x = 0, y = 0;
        for(int i = x - 4; i <= x + 4; i++){
            for(int j = y - 4; j <= y + 4; j++){
                if(distance(i, j, x, y) < 4){
                    System.out.printf("%d %d %f\n", i, j, distance(i, j, x, y));
                }
            }
        }
    }
     */
}
