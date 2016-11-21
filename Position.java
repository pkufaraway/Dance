public class Position{
    int x,y;
    public static int distance(Position p1, Position p2){
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    public int distance (Position p2){
        return Math.abs(this.x - p2.x) + Math.abs(this.y - p2.y);
    }

    public Position move(Position move){
        return new Position(this.x + move.x, this.y + move.y);
    }

    public Position(int x,int y){
        this.x = x;
        this.y = y;
    }

    public Position(Position p){
        this.x = p.x;
        this.y = p.y;
    }

    @Override
    public String toString(){
        return "(" + String.valueOf(x) + ", " + String.valueOf(y) + ")";
    }

    public boolean equals(Position p){
        return this.x == p.x && this.y == p.y;
    }

}