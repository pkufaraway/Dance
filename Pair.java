public class Pair {
    Position blue;
    Position red;
    public Pair(Position blue, Position red){
        this.blue = blue;
        this.red = red;
    }
    public int distance(){
        return Math.abs(this.blue.x - this.red.x) + Math.abs(this.blue.y - this.red.y);
    }
    @Override
    public String toString(){
        return this.blue.toString() + " " + this.red.toString();
    }
}
