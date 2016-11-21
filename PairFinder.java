import java.util.*;


public class PairFinder {
    private int size;
    private List<Position> blues;
    private List<Position> reds;

    public PairFinder(char[][] map){
        size = map.length;
        blues = new LinkedList<>();
        reds = new LinkedList<>();
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                if(map[i][j] == 'B'){
                    blues.add(new Position(i,j));
                    //System.out.println("New B" + new Position(i,j));
                } else if(map[i][j] == 'R'){
                    reds.add(new Position(i,j));
                    //System.out.println("New R" + new Position(i,j));
                }
            }
        }
    }

    public List<Pair> findPairs(int iterations){
        int maxDistance = Integer.MAX_VALUE;
        int minSum = Integer.MAX_VALUE;
        System.out.println(reds.size());
        System.out.println(blues.size());
        Set<Pair> pairs = new HashSet<>();
        Set<Pair> result = new HashSet<>();

        for(int i = 0; i < iterations; i++){
            pairs = greedyPairs();
            if(findMaxDistance(pairs) <= maxDistance){
                if(findMaxDistance(pairs) < maxDistance){
                    maxDistance = findMaxDistance(pairs);
                    result = pairs;
                }
                if(sumDistance(pairs) < minSum){
                    minSum = sumDistance(pairs);
                    result = pairs;
                }
            }
            Collections.shuffle(blues);
        }
        /*
        for (Map.Entry<Position, Position> entry : result.entrySet()) {
            System.out.printf("Blue %s Red %s\n", entry.getKey(), entry.getValue());
        }
        */
        System.out.println(sumDistance(result));
        System.out.println(findMaxDistance(result));
        return new LinkedList<>(result);
    }

    private HashMap<Pair, Pair> reduceSum(Set<Pair> pairs, int maxDistance) {
        HashMap<Pair, Pair> swap = new HashMap<>();
        for (Pair pairOne : pairs) {
            for (Pair pairTwo : pairs) {
                if(pairOne != pairTwo){
                    Position blueOne = pairOne.start;
                    Position blueTwo = pairTwo.start;
                    Position redOne = pairOne.end;
                    Position redTwo = pairTwo.end;
                    int d1 = Position.distance(blueOne, redTwo) + Position.distance(blueTwo, redOne);
                    int d2 = Position.distance(blueOne, redOne) + Position.distance(blueTwo, redTwo);
                    if(d1 < d2 && Position.distance(blueOne, redTwo) <= maxDistance && Position.distance(blueTwo, redOne) <= maxDistance){
                        //System.out.println(blueOne.toString() + redOne.toString() + blueTwo.toString() + redTwo.toString());
                        swap.put(pairOne, pairTwo);
                        return swap;
                    }
                }
            }
        }
        return swap;
    }

    private HashMap<Pair, Pair> reduceMax(Set<Pair> pairs, int maxDistance){
        HashMap<Pair, Pair> swap = new HashMap<>();
        for (Pair pairOne : pairs) {
            if(pairOne.distance() == maxDistance)
                for (Pair pairTwo : pairs) {
                    if(pairOne != pairTwo){
                        Position blueOne = pairOne.start;
                        Position blueTwo = pairTwo.start;
                        Position redOne = pairOne.end;
                        Position redTwo = pairTwo.end;
                        int d1 = Position.distance(blueOne, redTwo);
                        int d2 = Position.distance(blueTwo, redOne);
                        //System.out.println(blueOne.toString() + redOne.toString() + blueTwo.toString() + redTwo.toString());
                        int min = Math.max(d1, d2);
                        if(min < maxDistance){
                            swap.put(pairOne, pairTwo);
                            return swap;
                        }
                    }
                }
        }
        return swap;
    }

    private int findMaxDistance(Set<Pair> pairs){
        int answer = 0;
        for (Pair pair: pairs) {
            answer = Math.max(pair.distance(), answer);
        }
        return answer;
    }

    private int sumDistance(Set<Pair> pairs){
        int answer = 0;
        for (Pair pair: pairs) {
            answer += pair.distance();
        }
        return answer;
    }

    private Set<Pair> greedyPairs(){
        HashSet<Position> redsLeft = new HashSet<>(reds);
        Set<Pair> pairs = new HashSet<>();
        int maxDistance = 0;
        for(Position blue:blues) {
            int min = Integer.MAX_VALUE;
            Position choice = redsLeft.iterator().next();
            for (Position red : redsLeft) {
                if (Position.distance(blue, red) < min) {
                    min = Position.distance(blue, red);
                    choice = red;
                }
            }
            if(Position.distance(blue, choice) > maxDistance){
                maxDistance = Position.distance(blue, choice);
            }
            pairs.add(new Pair(blue, choice));
            redsLeft.remove(choice);
        }

        //Reduce maxDistance
        while(true){
            HashMap<Pair, Pair> swap = reduceMax(pairs, maxDistance);
            if(swap.isEmpty()){
                break;
            }
            for (Map.Entry<Pair, Pair> entry : swap.entrySet()) {
                Pair pairOne = entry.getKey();
                Pair pairTwo = entry.getValue();
                Position redOne = pairOne.end;
                Position redTwo = pairTwo.end;
                pairOne.end = redTwo;
                pairTwo.end = redOne;
                maxDistance = findMaxDistance(pairs);
            }
        }

        //Reduce sum of distance
        while(true){
            HashMap<Pair, Pair> swap = reduceSum(pairs, maxDistance);
            if(swap.isEmpty()){
                break;
            }
            for (Map.Entry<Pair, Pair>  entry : swap.entrySet()) {
                Pair pairOne = entry.getKey();
                Pair pairTwo = entry.getValue();
                Position redOne = pairOne.end;
                Position redTwo = pairTwo.end;
                pairOne.end = redTwo;
                pairTwo.end = redOne;
            }
        }

        return pairs;
    }
}
