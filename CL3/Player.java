/**
* Part of this class was provided by TAs (constructors and method headers)
* Most of methods were completed by @durazy
**/

import java.util.Random;
import java.util.Scanner;

public class Player {
	/*
	 * First inventory  row is usables
	 * Second inventory row is weapon
	 * Third inventory row is sweapons
	 * 
	 * For the equip inventory, slot 1 is weapon, slot 2 is sweapon
	 */

	//constants for categorizing main inventory
	public static final int USABLE = 0;
	public static final int WEAPON = 1;
	public static final int SWEAPON = 2;

	// attributes
	private String name;
	private int swordLevel;
	private int shieldLevel;
	private int maxHealth;
	private int healthPoints;
	private int keys = 0;
	private boolean hasMap;
	private boolean hasCompass;
	private boolean[][] hasVisited;
	private Item[][] inventory = new Item[3][];
	private Item[] equipInventory = new Item[2];

	// constructors
	public Player() {
	}

	public Player(String nameIn, int swrdLvl, int shldLvl, int maxHP, boolean hasMapIn, boolean hasCompassIn) {
		this.name = nameIn;
		this.swordLevel = swrdLvl;
		this.shieldLevel = shldLvl;
		this.maxHealth = maxHP;
		this.healthPoints = maxHP;
		this.hasMap = hasMapIn;
		this.hasCompass = hasCompassIn;

		//configure inventory
		this.inventory[USABLE] = new Item[2];
		this.inventory[WEAPON] = new Item[3];
		this.inventory[SWEAPON] = new Item[3];
	}



	// getters
	
	public String getName() {
		return name;
	}
	
	public int getSword() {
		return swordLevel;
	}
	public int getShield() {
		return shieldLevel;
	}

	public int getHealth() {
		return healthPoints;
	}

	public int getKeys() {
		return keys;
	}

	public boolean[][] getVisited() {
		return hasVisited;
	}

	public boolean getMap() {
		return hasMap;
	}

	public boolean getCompass() {
		return hasCompass;
	}

	
	// setters

	public void setVisited(boolean[][] visitedIn) {
		hasVisited = visitedIn;
	}

	public void updateMap() {
		hasMap = true;
	}

	public void updateCompass() {
		hasCompass = true;
	}
	
	public void updateKeys() {
		keys++;
	}
	
	public void useKey() {
		keys--;
	}
	
	public void increaseSword() {
		swordLevel++;
	}

	public void increaseShield() {
		shieldLevel++;
	}
	
	public void ouch(int damage) {
		healthPoints -= damage;
	}
	
	
	/*
	 * update Max health if health container is found
	 */
	public void updateMaxHealth() {
		maxHealth++;
		healthPoints++;
	}

	/*
	 * heals when healthpoints is less than or equal to maxhealth
	 */
	public void heal() {
		Random r = new Random();
		int healAmount = r.nextInt(maxHealth) + 1;
		healthPoints += healAmount;
		if (healthPoints > maxHealth) {
			healthPoints = maxHealth;
		}
		System.out.println(name + " restored " + healAmount + " healthPoints\n");
	}
	

	//methods
	
	/*
	 * Will return a usable item of type Item
	 */
	public Item getUsable(String name) {
		Item a = null;
		for (int i = 0; i < inventory[USABLE].length; i++) {
			a = inventory[USABLE][i];
			if (a != null && a.getName().equals(name)) {
				return a;
			}
		}
		return null;
	}
	
	/*
	 * To update the inventory if the item does not exist, if it does exist and its usable type,
	 * then increase the amount
	 * type 0 = usable
	 * type 1 = weapon
	 * type 2 = sweapon
	 */
	public void updateInventory(Item itemIn, boolean increase, int type) {
		if (checkInventory(itemIn.getName(), type)) {
			if (increase) {
				Item chosen = getUsable(itemIn.getName());
				chosen.increase();
			}	
		} else {
			//find first empty slot and set that to the itemIn being passed
			int index = 0;
			Item current = inventory[type][index];
			while (current != null && index < inventory[type].length) {
				index++;
				current = inventory[type][index];
			}
			inventory[type][index] = itemIn;
		}
	}

	/*
	 * To check the inventory and see if the item exists
	 * type 0 = usable
	 * type 1 = weapon
	 * type 2 = sweapon
	 */
	public boolean checkInventory(String name, int type){
		for (int i = 0; i < inventory[type].length; i++) {
			if (inventory[type][i] != null && inventory[type][i].getName().equals(name)) {
				return true;
			}
		}
		return false;
	}


	/*
	 * When an enemy is defeated, check for its dropped item.
	 * If its an endgame item, it will return true
	 */
	public boolean checkDrop(Enemy en) {
		String drop = en.getItem();
		if (drop.equals("relic")) {
			return true;
		} else if (drop.equals("heart")) {
			heal();
		} else if (drop.equals("sword")) {
			swordLevel++;
		} else if (drop.equals("shield")) {
			shieldLevel++;
		}
		return false;
	}
	 

	/*
	 * To equip a weapon or a special weapon
	 */
	public void updateEquip(int index, String type) {
		if (type.equals("weapon")) {
			if (inventory[WEAPON][index] != null) {
				equipInventory[0] = inventory[WEAPON][index];
			}
		} else {
			if (inventory[SWEAPON][index] != null) {
				equipInventory[1] = inventory[SWEAPON][index];
			}
		}

	}

	/*
	 * Gets the item equipped and use it in battle
	 * if type = 1, gets Weapon
	 * if type = 2, gets sWeapon
	 */
	public Item useEquip(int type) {
		type--;
		return equipInventory[type];
	}
	
	/*
	 * updates the visited coordinate to true
	 */
	public void updateVisited(int yCoord, int xCoord) {
		hasVisited[yCoord][xCoord] = true;
	}

	/*
	 * Prints a menu of weapons for the player to choose from based on the type the player
	 * has chosen. The type determines which row in the inventory array to read from.
	 */
	public void printWeps(int type) {
		for (int i = 0; i < inventory[type].length; i++) {
			System.out.print(i + ":");
			if (inventory[type][i] != null) {
				System.out.println(inventory[type][i].getName());
			} else {
				System.out.println("null");
			}
		}
	}

	/*
	 * prints out the player's current inventory
	 */
	public void printInventory() {
		String[] categories = {"Usables", "Weapons", "Special Weapons"};
		System.out.println("- Current inventory -");
		for (int i = 0; i < inventory.length; i++) {
			System.out.print(categories[i] + ": ");
			for (int j = 0; j < inventory[i].length; j++) {
				if (inventory[i][j] != null) {
					System.out.print(inventory[i][j].getName());
					if (i == 0) {
						System.out.print(" x" + inventory[i][j].getAmount());
					}
				} else {
					System.out.print("null");
				}
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	/*
	 * To print all the equipment that the player has
	 */
	public void printEquip() {
		System.out.println("- Currently equipped -");
		System.out.print("Weapon: ");
		if (equipInventory[0] != null) {
			System.out.println(equipInventory[0].getName());
		} else {
			System.out.println("empty");
		}
		System.out.print("Special Weapon: ");
		if (equipInventory[1] != null) {
			System.out.println(equipInventory[1].getName());
		} else {
			System.out.println("empty");
		}
	}



	//returns the player info as a string
	public String toString() {
		return "Name: " + this.name + "\nSwordLV: " + this.swordLevel + "\nShieldLV: " + this.shieldLevel
				+ "\nHasMap: " + this.hasMap + "\nHasCompass: " + this.hasCompass + "\nMaxHP: " + maxHealth;
	}



}
