import java.util.*;

public class PathPlanner {
    private List<Pair> pairs;
    private char[][] map;
    private int size;
    private LinkedList<Position> movement;
    public PathPlanner(char[][] map, List<Pair> pairs){
        this.map = map;
        this.pairs = pairs;
        this.size = map.length;
        movement = new LinkedList<>();
        movement.add(new Position(1,0));
        movement.add(new Position(-1,0));
        movement.add(new Position(0,1));
        movement.add(new Position(0,-1));

        Collections.sort(pairs, new Comparator<Pair>() {
                @Override
                public int compare(Pair o1, Pair o2) {
                    return o2.distance() - o1.distance();
                }
            });
    }

    private boolean isValid(Position p){
        return p.x >= 0  && p.x < size && p.y > 0 && p.y < size && map[p.x][p.y] != 'S';
    }

    private LinkedList<LinkedList<Position>> pathGenerator(Position blue, Position red, Position[][] parents) {
        LinkedList<Position> bluePath = new LinkedList<>();
        while (blue != null) {
            bluePath.add(blue);
            blue = parents[blue.x][blue.y];
        }
        LinkedList<Position> redPath = new LinkedList<>();
        while (red != null) {
            redPath.add(red);
            red = parents[red.x][red.y];
        }
        Collections.reverse(bluePath);
        Collections.reverse(redPath);
        LinkedList<LinkedList<Position>> result = new LinkedList<>();
        result.add(bluePath);
        result.add(redPath);
        return result;
    }

    private LinkedList<LinkedList<Position>> findSinglePath(Position blue, Position red) {
        Queue<Position> blueQ = new LinkedList<>();
        Queue<Position> redQ = new LinkedList<>();
        Position[][] parents = new Position[size][size];
        int[][] color = new int[size][size];
        int[][] steps = new int[size][size];
        blueQ.add(blue);
        redQ.add(red);
        color[blue.x][blue.y] = 1;
        color[red.x][red.y] = 2;
        Position currentblue = blue;
        Position currentred = red;
        int i = 0;
        while(!blueQ.isEmpty() && !redQ.isEmpty()){
            currentblue = blueQ.peek();
            currentred = redQ.peek();
            if(steps[currentblue.x][currentblue.y] <= steps[currentred.x][currentred.y]) {
                blueQ.poll();
                for(Position move : movement){
                    Position p = currentblue.move(move);
                    if(isValid(p)){
                        if(color[p.x][p.y] == 0) {
                            color[p.x][p.y] = 1;
                            parents[p.x][p.y] = currentblue;
                            steps[p.x][p.y] = steps[currentblue.x][currentblue.y] + 1;
                            blueQ.add(p);
                        } else if(color[p.x][p.y] == 2 && map[p.x][p.y] != 'F' && map[currentblue.x][currentblue.y] != 'F'){
                            return pathGenerator(currentblue, new Position(p.x, p.y), parents);
                        }
                    }
                }
            } else {
                redQ.poll();
                for(Position move : movement){
                    Position p = currentred.move(move);
                    if(isValid(p)){
                        if(color[p.x][p.y] == 0) {
                            color[p.x][p.y] = 2;
                            parents[p.x][p.y] = currentred;
                            steps[p.x][p.y] = steps[currentred.x][currentred.y] + 1;
                            redQ.add(p);
                        } else if(color[p.x][p.y] == 1 && map[p.x][p.y] != 'F' && map[currentred.x][currentred.y] != 'F'){
                            return pathGenerator(new Position(p.x, p.y), currentred, parents);
                        }
                    }
                }
            }
        }
        return new LinkedList<>();
    }

    public LinkedList<LinkedList<Position>> findPath(){
        LinkedList<LinkedList<Position>> result = new LinkedList<>();
        for (Pair pair: pairs) {
            Position blue = pair.blue;
            Position red = pair.red;
            LinkedList<LinkedList<Position>> paths = findSinglePath(blue,red);
            result.addAll(paths);
            for(LinkedList<Position> path : paths){
                Position end = path.getLast();
                System.out.println(end);
                map[end.x][end.y] = 'F';
            }

        }
        return result;
    }

    public static void main(String[] args){
        MapParser parser = new MapParser("test_input1.txt", 30);

        PairFinder pairFinder = new PairFinder(parser.map);
        Helper.printMap(parser.map);
        List<Pair> pairs = pairFinder.findPairs(50000);
        for(Pair pair:pairs){
            System.out.println(pair.toString());
        }

        PathPlanner pathPlanner = new PathPlanner(parser.map, pairs);
        LinkedList<LinkedList<Position>> paths = pathPlanner.findPath();
        for(LinkedList<Position> path : paths){
            for (Position i :path){
                System.out.print(i.toString() + "->");
            }
            System.out.println();
        }
        //Helper.printMap(pathPlanner.map);
    }
}
