import java.util.*;

public class PathPlanner {
    private List<Pair> pairs;
    private char[][] map;
    private int size;
    private LinkedList<Position> movement;

    public PathPlanner(char[][] map, List<Pair> pairs) {
        this.pairs = pairs;
        this.size = map.length;
        this.map = new char[size][size];
        Helper.copyMap(map, this.map);
        movement = new LinkedList<>();
        movement.add(new Position(1, 0));
        movement.add(new Position(-1, 0));
        movement.add(new Position(0, 1));
        movement.add(new Position(0, -1));

        Collections.sort(pairs, new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return o2.distance() - o1.distance();
            }
        });
    }

    private boolean isValid(Position p) {
        return p.x >= 0 && p.x < size && p.y > 0 && p.y < size && map[p.x][p.y] != 'S';
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

    private List<Pair> findPositions(Position blue, Position red) {
        int min = Integer.MAX_VALUE;
        List<Pair> answer = new ArrayList<>();
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                Position p = new Position(i, j);
                for (Position move : movement) {
                    Position p1 = p.move(move);
                    if (isValid(p1) && map[p.x][p.y] != 'F' && map[p1.x][p1.y] != 'F') {
                        int distance = p1.distance(blue) + p.distance(red);
                        if (distance < min) {
                            min = distance;
                            answer = new ArrayList<>();
                            answer.add(new Pair(blue, p1));
                            answer.add(new Pair(red, p));
                        }
                        distance = p.distance(blue) + p1.distance(red);

                        if (distance < min) {
                            min = distance;
                            answer = new ArrayList<>();
                            answer.add(new Pair(blue, p));
                            answer.add(new Pair(red, p1));
                        }
                    }
                }
            }
        return answer;
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

        while (!blueQ.isEmpty() && !redQ.isEmpty()) {
            currentblue = blueQ.peek();
            currentred = redQ.peek();
            if (steps[currentblue.x][currentblue.y] <= steps[currentred.x][currentred.y]) {
                blueQ.poll();
                for (Position move : movement) {
                    Position p = currentblue.move(move);
                    if (isValid(p)) {
                        if (color[p.x][p.y] == 0) {
                            color[p.x][p.y] = 1;
                            parents[p.x][p.y] = currentblue;
                            steps[p.x][p.y] = steps[currentblue.x][currentblue.y] + 1;
                            blueQ.add(p);
                        } else if (color[p.x][p.y] == 2 && map[p.x][p.y] != 'F' && map[currentblue.x][currentblue.y] != 'F') {
                            return pathGenerator(currentblue, new Position(p.x, p.y), parents);
                        }
                    }
                }
            } else {
                redQ.poll();
                for (Position move : movement) {
                    Position p = currentred.move(move);
                    if (isValid(p)) {
                        if (color[p.x][p.y] == 0) {
                            color[p.x][p.y] = 2;
                            parents[p.x][p.y] = currentred;
                            steps[p.x][p.y] = steps[currentred.x][currentred.y] + 1;
                            redQ.add(p);
                        } else if (color[p.x][p.y] == 1 && map[p.x][p.y] != 'F' && map[currentred.x][currentred.y] != 'F') {
                            return pathGenerator(new Position(p.x, p.y), currentred, parents);
                        }
                    }
                }
            }
        }
        return new LinkedList<>();
    }

    private LinkedList<Position> singleBFS(Pair pair) {
        int[][] color = new int[size][size];
        Position[][] parents = new Position[size][size];
        Position start = pair.start;
        Position end = pair.end;
        Queue<Position> toVisit = new LinkedList<>();
        toVisit.add(start);
        while (!toVisit.isEmpty()) {
            Position currentPosition = toVisit.poll();
            color[currentPosition.x][currentPosition.y] = 2;
            if (currentPosition.equals(pair.end)) {
                LinkedList<Position> answer = new LinkedList<>();
                Position currentEnd = currentPosition;
                while (currentEnd != null) {
                    answer.add(currentEnd);
                    currentEnd = parents[currentEnd.x][currentEnd.y];
                }
                Collections.reverse(answer);
                return answer;
            }
            for (Position move : movement) {
                Position newPosition = currentPosition.move(move);
                if (isValid(newPosition) && color[newPosition.x][newPosition.y] == 0) {
                    toVisit.add(newPosition);
                    parents[newPosition.x][newPosition.y] = currentPosition;
                    color[newPosition.x][newPosition.y] = 1;
                }

            }
        }
        return new LinkedList<>();
    }

    public LinkedList<LinkedList<Position>> findPath() {
        LinkedList<LinkedList<Position>> result = new LinkedList<>();
        for (Pair pair : pairs) {
            Position blue = pair.start;
            Position red = pair.end;
            LinkedList<LinkedList<Position>> paths = findSinglePath(blue, red);
            if (paths.size() == 0) {
                paths.clear();
                List<Pair> newPairs = findPositions(blue, red);
                for (Pair newPair : newPairs) {
                    LinkedList<Position> newPath = singleBFS(newPair);
                    paths.add(newPath);
                }
            }
            result.addAll(paths);
            for (LinkedList<Position> path : paths) {
                Position end = path.getLast();
                map[end.x][end.y] = 'F';
            }

        }
        return result;
    }
}
