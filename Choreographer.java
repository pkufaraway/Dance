import java.util.*;

public class Choreographer {
    LinkedList<LinkedList<Position>> paths;
    int[] indices;
    int size;
    char[][] map;
    public Choreographer(char[][] map) {
        this.map = map;
        size = map.length;
        PairFinder pairFinder = new PairFinder(map);
        List<Pair> pairs = pairFinder.findPairs(50000);
        for(Pair pair:pairs){
            System.out.println(pair.toString());
        }
        PathPlanner pathPlanner = new PathPlanner(map, pairs);
        paths = pathPlanner.findPath();
        indices = new int[paths.size()];
    }

    public ArrayList<Pair> duplicateSolver(ArrayList<Pair> baiscmoves){
        boolean[][] end = new boolean[size][size];
        boolean[][] duplicate = new boolean[size][size];
        for(Pair pair : baiscmoves){
            if((end[pair.red.x][pair.red.y])){
                duplicate[pair.red.x][pair.red.y] = true;
            }
            end[pair.red.x][pair.red.y] = true;
        }

        ArrayList<Position> duplicateSet = new ArrayList<>();
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(duplicate[i][j]){
                    duplicateSet.add(new Position(i,j));
                }
            }
        }

        System.out.println(duplicateSet.size());
        for(Position p: duplicateSet){
            ArrayList<Pair> currentLock = new ArrayList<>();
            for(Pair pair : baiscmoves){
                if(pair.red.equals(p)){
                    System.out.println("Duplicate");
                    System.out.println(pair);
                    currentLock.add(pair);
                }
            }
            Pair center = null;
            for(Pair pair : currentLock){
                if(pair.blue.equals(p) && pair.red.equals(p)){
                    center = pair;
                    break;
                }
            }
            Pair head = currentLock.get(0);
            if (center != null){
                for(Pair pair : currentLock){
                    if(pair != center){
                        head = pair;
                        break;
                    }
                }
                System.out.println("Head" + head.toString());
                System.out.println("Center" + center.toString());
                for(int i = 0; i < paths.size(); i++){
                    if(paths.get(i).get(indices[i]).equals(center.red) && indices[i] == paths.get(i).size() - 1){
                        paths.get(i).get(indices[i]).x = head.blue.x;
                        paths.get(i).get(indices[i]).y = head.blue.y;
                        paths.get(i).addLast(new Position(head.red.x, head.red.y));
                        System.out.println(paths.get(i).get(indices[i]));
                        System.out.println(paths.get(i).get(indices[i]+1));
                        break;
                    }
                }
                System.out.println("Head" + head.toString());
                center.red = new Position(head.blue.x, head.blue.y);
                center.blue = new Position(head.red.x, head.red.y);
                System.out.println("Center" + center.toString());
            }
            for(Pair pair : currentLock){
                if(pair != head && pair != center){
                    for(int i = 0; i < paths.size(); i++){
                        if(indices[i] > 0 && paths.get(i).get(indices[i]).equals(pair.red) && paths.get(i).get(indices[i] - 1).equals(pair.blue)){
                            indices[i]--;
                        }
                    }
                    pair.red.x = pair.blue.x;
                    pair.red.y = pair.blue.y;
                }
            }
        }

        if (duplicateSet.size() > 0){
            return duplicateSolver(baiscmoves);
        } else {
            return baiscmoves;
        }
    }

    public ArrayList<Pair> moveGenerator(){
        ArrayList<Pair> moves = new ArrayList<>();
        for(int i = 0; i < paths.size(); i++){
            if(indices[i] == paths.get(i).size() - 1){
                moves.add(new Pair(paths.get(i).get(indices[i]),
                        paths.get(i).get(indices[i])));
            } else {
                moves.add(new Pair(paths.get(i).get(indices[i]),
                        paths.get(i).get(++indices[i])));
            }
        }
        return duplicateSolver(moves);
    }

    public String getMoves() {
        StringBuilder builder = new StringBuilder();
        System.out.println("Moves");
        ArrayList<Pair> moves = moveGenerator();
        for (Pair pair : moves) {
            if(!pair.red.equals(pair.blue)) {
                builder.append(pair.blue.x);
                builder.append(' ');
                builder.append(pair.blue.y);
                builder.append(' ');
                builder.append(pair.red.x);
                builder.append(' ');
                builder.append(pair.red.y);
                builder.append(' ');
                System.out.println(pair.blue.toString() + pair.red.toString());
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append('\n');
        return builder.toString();
    }

/*
    public static void main(String args[]) {
        MapParser parser = new MapParser("test_input.txt", 30);
        Choreographer choreographer = new Choreographer(parser.map);
        choreographer.getMoves();

    }
*/

}
