public class Pair {
    Position start;
    Position end;
    char color = 0;
    public Pair(Position start, Position end, char color){
        this.start = start;
        this.end = end;
        this.color = color;
    }
    public Pair(Position start, Position end){
        this.start = start;
        this.end = end;
    }
    public int distance(){
        return Math.abs(this.start.x - this.end.x) + Math.abs(this.start.y - this.end.y);
    }
    @Override
    public String toString(){
        return this.start.toString() + " " + this.end.toString();
    }
}
