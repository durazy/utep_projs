/**
* Provided by TAs
**/

public class Enemy {
    private String name;
    private int healthPoints;
    private int attackPower;
    private String itemDrop;

    public Enemy() {
    }

    public Enemy(String nameIn, int healthIn, int attackPowerIn, String itemIn) {
        this.name = nameIn;
        this.healthPoints = healthIn;
        this.attackPower = attackPowerIn;
        this.itemDrop = itemIn;
    }

    // setters
    public void setName(String nameIn) {
        this.name = nameIn;
    }

    public void setHealth(int healthIn) {
        this.healthPoints = healthIn;
    }

    public void setItem(String itemIn) {
        this.itemDrop = itemIn;
    }

    public void setPower(int attackIn) {
        this.attackPower = attackIn;
    }

    // getters
    public String getName() {
        return this.name;
    }

    public int getHealth() {
        return this.healthPoints;
    }

    public String getItem() {
        return this.itemDrop;
    }

    public int getPower() {
        return this.attackPower;
    }
}
