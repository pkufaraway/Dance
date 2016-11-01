import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Spoiler {
    char[][] map;
    int[][] heatMap;

    private final Integer numberOfStars;
    private final Integer size;
    private final List<Position> starLocations;
    private int starsLeftToPlace;
    
    private final PairFinder pairFinder;
    private final List<Pair> pairs;
    private final PathPlanner paths;
    private final LinkedList<LinkedList<Position>> routes;

    public Spoiler(char[][] map, int numberOfStars){
        this.numberOfStars = numberOfStars;
        System.out.println(numberOfStars);
        this.map = map;
        size = map.length;
        starLocations = new ArrayList<Position>();
        starsLeftToPlace = numberOfStars;
        Helper.printMap(map);

        pairFinder = new PairFinder(map);
        pairs = pairFinder.findPairs(50000);
        paths = new PathPlanner(map, pairs);
        routes = paths.findPath();
        heatMap = new int[size][size];
        populateHeatMap();
        
        placeStars();
        System.out.println("Finished placing stars");
    }

    // This should be manhattan distance - not euclidean distance
    private static int distance(int x1,int y1, int x2, int y2){
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }
    
    /***
     * Validity checker for stars. Looks at the stars already placed, and
     * ensures they are not too close. Ensures that a dancer is occupying the
     * space and that the coordinates are within bounds.
     * @param x x coordinate
     * @param y y coordinate
     * @return Whether placing a star at x, y is valid
     */
    private boolean isValid(int x, int y) {
      if (map[x][y] != 0 || x < 0 || y < 0 || x >= size || y >= size) {
        return false;
      }
      
      for (Position star : starLocations) {
        if (distance(star.x, star.y, x, y) < 4) {
          return false;
        }
      }
      return true;
    }
    
    private Position findHottestSpotInPaths(int pairIndex) {
      LinkedList<Position> bluePath = routes.get(pairIndex);
      LinkedList<Position> redPath = routes.get(pairIndex + 1);
      
      Position hottestSpot = new Position(-1, -1);
      int hottestRank = 0;
      
      for (Position bluePosition : bluePath) {
        if (heatMap[bluePosition.x][bluePosition.y] > hottestRank) {
          hottestSpot = bluePosition;
          hottestRank = heatMap[bluePosition.x][bluePosition.y];
        }
      }
      for (Position redPosition : redPath) {
        if (heatMap[redPosition.x][redPosition.y] > hottestRank) {
          hottestSpot = redPosition;
          hottestRank = heatMap[redPosition.x][redPosition.y];
        }
      }
      return hottestSpot;
    }
    
    /***
     * Populate the heat map based on the paths we predict the user to take.
     */
    private void populateHeatMap() {
      for (LinkedList<Position> routeOfDancer : routes) {
        for (Position position: routeOfDancer) {
          heatMap[position.x][position.y] += 1;
        }
      }
      
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          if (map[i][j] != 0) {
            heatMap[i][j] = 0;
          }
        }
      }
    }
    
    /***
     * Find the maximum location on the heat map
     * @return Position of hottest spot based on predicted routes
     */
    private Position findMaxHeatMap() {
      int max = 0;
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          if (heatMap[i][j] > max) {
            return new Position(i, j);
          }
        }
      }
      return new Position(-1, -1);
    }
    
    /***
     * Find the index in the pairs list for the pair that is furthest
     * apart.
     * @return
     */
    private int getFurthestPredictedPairIndex() {
      Pair furthestPair = pairs.get(0);
      int pairIndex = 0;
      
      for (int i = 1; i < pairs.size(); i++) {
        if (pairs.get(i).distance() > furthestPair.distance()) {
          furthestPair = pairs.get(i);
          pairIndex = i;
        }
      }
      return pairIndex;
    }
    
    private boolean getCongestionStar() {
      Position star;
      while (starsLeftToPlace > 0) {
        System.out.println("Trying to get a congestive star");
        star = findMaxHeatMap();
        if (star.x != -1 && star.y != -1) {
          heatMap[star.x][star.y] = 0;
          if (isValid(star.x, star.y)) {
            starLocations.add(star);
            starsLeftToPlace--;
            return true;
          }
        } else {
          return false;
        }
      }
      return true;
    }
    
    private boolean getBlockingStar() {
      Position star;
      while (starsLeftToPlace > 0) {
        System.out.println("Trying to get a blocking star");
        int furthestPairIndex = getFurthestPredictedPairIndex();
        star = findHottestSpotInPaths(furthestPairIndex);
        Pair furthestPair = pairs.get(furthestPairIndex);
        System.out.println("FurthestIndexPair : " + furthestPairIndex);
        if (star.x == -1 && star.y == -1) {
          //Place them on top of each other. We've already computed the
          //route they're likely to take. This will just help us find the
          //next furthest pair.
          System.out.println("Placing pairs on top of each other");
          furthestPair.blue.x = furthestPair.red.x;
          furthestPair.blue.y = furthestPair.red.y;
        } else {
          heatMap[star.x][star.y] = 0;
          if (isValid(star.x, star.y)) {
            System.out.println("Placed a star");
            starLocations.add(star);
            starsLeftToPlace--;
            return true;
          }
        }
      }
      return true;
    }
    
    private void placeStars() {
      int numberOfCongestionStars = numberOfStars / 2;
      boolean gotStar = false;
      for (int i = 0; i < numberOfCongestionStars; i++) {
        gotStar = getCongestionStar();
        if (!gotStar) {
          break;
        }
      }
      while (starsLeftToPlace > 0) {
        getBlockingStar();
      }
    }
    

    
    public String getStars() {
      StringBuilder formattedOutput = new StringBuilder();
      for (Position star : starLocations) {
        formattedOutput.append(star.x);
        formattedOutput.append(" ");
        formattedOutput.append(star.y);
        formattedOutput.append(" ");
      }
      formattedOutput.deleteCharAt(formattedOutput.length() - 1);
      return formattedOutput.toString();
    }

}
