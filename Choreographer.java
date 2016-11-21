import javafx.geometry.Pos;

import java.util.*;

public class Choreographer {
    LinkedList<Path> paths;
    int size;
    char[][] map;
    public Choreographer(char[][] map) {
        this.map = map;
        size = map.length;
        PairFinder pairFinder = new PairFinder(map);
        List<Pair> pairs = pairFinder.findPairs(50000);
        PathPlanner pathPlanner = new PathPlanner(map, pairs);
        LinkedList<LinkedList<Position>> simplePaths = pathPlanner.findPath();
        System.out.println("Finished Pairs and simple Paths");

        Collections.sort(simplePaths, new Comparator<LinkedList<Position>>() {
            @Override
            public int compare(LinkedList<Position> o2, LinkedList<Position> o1) {
                return o1.size() - o2.size();
            }
        });

        paths = new LinkedList<>();
        for(LinkedList<Position> path : simplePaths){
            Position start = path.getFirst();
            paths.add(new Path(path,map[start.x][start.y]));
        }
    }

    public List<Pair> moveGenerator(){
        List<Pair> moves = new LinkedList<>();
        for(Path path: paths){
            path.resolved = false;
            path.priority = path.path.size() - path.currentIndex;
        }
        resolveCircle();
        lockResolver();
        for(Path path: paths) {
            Pair move = path.moveForward();
            moves.add(move);
        }
        return moves;
    }

    public void resolveCircle(){

        int[][] color = new int[size][size];
        Position[][] next = new Position[size][size];
        for(Path path: paths) {
            Pair move = path.peekMove();
            if(!path.resolved && !move.start.equals(move.end)) {
                next[move.start.x][move.start.y] = move.end;
            }
        }
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(color[i][j] == 0 && next[i][j] != null){
                    boolean hasCircle = false;
                    color[i][j] = 1;
                    Position p = next[i][j];
                    while(p != null){
                        if(color[p.x][p.y] == 1){
                            System.out.println("Has circle");
                            hasCircle = true;
                            break;
                        } else if(color[p.x][p.y] == 0){
                            color[p.x][p.y] = 1;
                        } else {
                            break;
                        }
                        p = next[p.x][p.y];
                    }
                    if(!hasCircle){
                        color[i][j] = 2;
                        p = next[i][j];
                        while(p != null && color[p.x][p.y] < 2){
                            color[p.x][p.y] = 2;
                            p = next[p.x][p.y];
                        }
                    } else {
                        int x = p.x;
                        int y = p.y;
                        color[x][y] = 3;
                        p = next[p.x][p.y];
                        while(p != null && (p.x != x || p.y != y)){
                            color[p.x][p.y] = 3;
                            p = next[p.x][p.y];
                        }

                    }
                }
            }
        }
        for(Path path: paths) {
            Pair move = path.peekMove();
            if(color[move.start.x][move.start.y] == 3){
                path.resolved = true;
            }
        }

    }
    public void lockResolver(){
        boolean lock = false;
        Position lockP = new Position(-1, -1);
        for(Path path: paths){
            Pair move = path.peekMove();
            for(Path otherPath: paths){
                if(otherPath != path) {
                    Pair otherMove = otherPath.peekMove();
                    if (otherMove.end.equals(move.end)) {
                        lock = true;
                        lockP = otherMove.end;
                        break;
                    }
                }
            }
            if(lock){
                break;
            }
        }
        if(lock){
            List<Path> locks = new ArrayList<>();
            for(Path path: paths){
                Pair move = path.peekMove();
                if(move.end.equals(lockP) || move.start.equals(lockP)){
                    locks.add(path);
                }
            }
            locks.sort(new Comparator<Path>() {
                @Override
                public int compare(Path o1, Path o2) {
                    return o2.priority - o1.priority;
                }
            });

            Path firstPath = locks.get(0);
            for(Path path : locks){
                if(path.resolved){
                    firstPath = path;
                    break;
                }
            }

            Pair firstPathMove = firstPath.peekMove();
            firstPath.resolved = true;
            for(Path path: locks){
                if(path != firstPath && !path.resolved){
                    Pair move = path.peekMove();
                    if(move.start.equals(firstPathMove.end)){
                        if(!move.end.equals(firstPathMove.start)) {
                            path.insertGobackMove(new Position(firstPathMove.start));
                            path.priority = Integer.MAX_VALUE;
                            path.resolved = true;
                        }
                    } else {
                        path.insertStay();
                        path.priority = Integer.MAX_VALUE;
                        path.resolved = true;
                    }
                }
            }
            lockResolver();
        }
    }


    public String getMoves() {
        StringBuilder builder = new StringBuilder();
        List<Pair> moves = moveGenerator();
        boolean[][] visted = new boolean[size][size];
        System.out.println("Moves");
        for (Pair pair : moves) {
            if(!pair.end.equals(pair.start)) {

                builder.append(pair.start.x);
                builder.append(' ');
                builder.append(pair.start.y);
                builder.append(' ');
                builder.append(pair.end.x);
                builder.append(' ');
                builder.append(pair.end.y);
                builder.append(' ');
                System.out.println(pair.start.toString() + pair.end.toString());
            }
        }
        Helper.refreshMap(moves, map);
        System.out.println();
        if(builder.length() > 0){builder.deleteCharAt(builder.length() - 1);}
        builder.append('\n');
        return builder.toString();
    }

    public static void main(String args[]) {
        MapParser parser = new MapParser("test_input1.txt", 3);
        Helper.printMap(parser.map);
        System.out.println();
        Choreographer choreographer = new Choreographer(parser.map);
        parser = new MapParser("test_input1.txt", 3);
        choreographer.map = parser.map;
        for(int i = 0; i < 10; i++) {
            choreographer.getMoves();
            System.out.printf("Iterations %d\n", i);
        }
    }

}
