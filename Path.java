import java.util.ArrayList;
import java.util.LinkedList;

public class Path {
    ArrayList<Position> path;
    int priority;
    int currentIndex;
    boolean resolved;
    char color = 'B';
    public Path(LinkedList<Position> path, char color){
        this.path = new ArrayList<>(path);
        this.priority = path.size();
        this.color = color;
    }

    public Pair moveForward(){
        this.priority--;
        if(currentIndex < path.size() - 1) {
            currentIndex++;
            return new Pair(path.get(currentIndex - 1), path.get(currentIndex),color);
        } else {
            return new Pair(path.get(currentIndex), path.get(currentIndex),color);
        }
    }

    public static boolean pinned(boolean[][] pin, Position p){
        return pin[p.x][p.y];
    }

    public Pair peekMove(){
        if(currentIndex < path.size() - 1) {
            return new Pair(path.get(currentIndex), path.get(currentIndex + 1));
        } else {
            return new Pair(path.get(currentIndex), path.get(currentIndex));
        }
    }

    public void insertGobackMove(Position p){

        System.out.println("Gobackmove");
        print();

        if(currentIndex != path.size() - 1){
            path.add(currentIndex + 1, new Position(path.get(currentIndex)));
            path.add(currentIndex + 1, p);
        } else {
            path.add(p);
            path.add(new Position(path.get(currentIndex)));
        }
        print();
        this.priority++;
    }

    public void insertStay(){

        System.out.println("Staymove");
        print();

        if(currentIndex != path.size() - 1){
            path.add(currentIndex, new Position(path.get(currentIndex)));
            this.priority++;
        }
        print();
    }

    public void print(){
        //System.out.printf("Current %d postion: %s", currentIndex, path.get(currentIndex));
        for(int i = currentIndex; i < path.size(); i++){
            System.out.print(path.get(i).toString() + "->");
        }
        /*
        for(Position p : path){
            System.out.print(p.toString() + "->");
        }*/
        System.out.println();
    }
}
