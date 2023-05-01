public class Node {
    // attributes
    public Player player;
    public Enemy enemy;
    public Node next;
    public int num;

    // constructors
    public Node() {
        this.next = null;
    }

    public Node(Player playerIn) {
        this.player = playerIn;
        this.next = null;
    }

    public Node(Enemy enemyIn) {
        this.enemy = enemyIn;
        this.next = null;
    }
    
}
