public class Item {

	// attributes
	private String name;
	private String type;
	private int amount = 1;
	private int dmg;

	/*
	 * Creates an item with passed with given name, type, and damage
	 * Items of type usable/heal have a max of INF
	 * Items of type sWeapon or Weapon have a max of 1
	 * Upgrade has a max of 7
	 * If an item with an amount greater than 0 is obtained, it is removed from the Items array
	 * in the main file.
	 */
	public Item(String nameIn, String typeIn, int dmgIn) {
		this.name = nameIn;
		this.type = typeIn;
		this.dmg = dmgIn;
		if (typeIn.equals("upgrade")) {
			this.amount = dmgIn;
		}
	}


	//setters

	public void setAmount(int amountIn) {
		this.amount = amountIn;
	}

	public void setDmg(int dmgIn) {
		this.dmg = dmgIn;
	}



	//getters

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public int getAmount() {
		return this.amount;
	}

	public int getDmg() {
		return this.dmg;
	}



	//methods

	//will decrease the amount if item is usable
	public void use() {
		this.amount--;
	}

	//increase the amount if item is usable
	public void increase() {
		this.amount++;
	}


}