import java.util.*;

public class Choreographer {
    char[][] map;
    int size;

    public Choreographer(char[][] map) {
        this.map = map;
        size = map.length;
    }

    public String getMoves() {
        HashMap<Integer, Position> moves = new HashMap<>();
        return "\n";
    }

    /*
    public static void main(String args[]) {
        MapParser parser = new MapParser("test_input.txt", 30);
        PairFinder pairFinder = new PairFinder(parser.map);
        List<Pair> pairs = pairFinder.findPairs(50000);
        for(Pair pair:pairs){
            System.out.println(pair.toString());
        }
        PathPlanner pathPlanner = new PathPlanner(parser.map, pairs);
        HashMap<Position, LinkedList<Position>> paths = pathPlanner.findPath();
        for(Pair pair:pairs){
            System.out.printf("Pair %s 's path\n", pair.toString());
            Position p = pair.blue;
            System.out.print(p.toString() + ": ");
            LinkedList<Position> path = paths.get(p);
            for (Position i :path){
                System.out.print(i.toString() + "->");
            }
            System.out.println();

            p = pair.red;
            System.out.print(p.toString() + ": ");
            path = paths.get(p);
            for (Position i :path){
                System.out.print(i.toString() + "->");
            }
            System.out.println();
        }
    }


    */

}
