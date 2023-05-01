/* Daniel Duru
[CS1101 - FA22] Comprehensive Lab 3
This work is to be done individually. It is not
permitted to share, reproduce, or alter any part of
this
assignment for any purpose. Students are not
permitted from sharing code, uploading this
assignment online in any form, or
viewing/receiving/modifying code written from
anyone else. This
assignment is part of an academic course at The
University of Texas at El Paso and a grade will be
assigned for the work produced individually by
the student. */

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

public class CL3{

	public static final int NORTH = 0;
	public static final int SOUTH = 1;
	public static final int WEST = 2;
	public static final int EAST = 3;
	public static final int ER_LIMIT = 100;
	public static final int HALVE = 1;
	public static final int RESET = 2;
	public static final int PLAYER_WIN = 0;
	public static final int GAME_OVER = 68;


	/*
	 * Gathers all necessary assets and starts the game with those Assets.
	 */
	public static void main(String[] args) {

		String[][] dungeon = loadDungeon();
		Enemy[] enemies = readEnemies();
		int[] playerCurr = dungeonEntrance(dungeon);
		Item[] items = getItems();
		Player player = createPlayer("Player");
		player.setVisited(new boolean[dungeon.length][dungeon[0].length]);
		gameStart(player,dungeon,playerCurr,enemies,items);
	}



	/**
	 * Loading Methods and file reading methods 
	 **/

	/*
	 * Read the dungeon excel file and return the values in the format of
	 * a 2D array.
	 */
	public static String[][] loadDungeon() {
		int rows = numLines("Dungeon.csv");
		String[][] result = new String[rows][];

		try {
			File dungeonSource = new File("Dungeon.csv");
			Scanner reader = new Scanner(dungeonSource);
			int col = 0;
			while (reader.hasNext()) {
				String[] currentLine = reader.next().split(",", 0);
				result[col] = currentLine;
				col++;
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("dungeon file not found");
		}
		return result;
	}




	/*
	 * To locate the entrance of a dungeon
	 * return two indices, first is the row (yCoord), second is the column (xCoord)
	 */
	public static int[] dungeonEntrance(String[][] dungeon) {
		for (int i = 0; i < dungeon.length; i++) {
			for (int j = 0; j < dungeon[i].length; j++) {
				if (dungeon[i][j].equals("e")) {
					return new int[] {i, j};
				}
			}
		}
		return new int[2];
	}

	/*
	 * Read and get enemies types from a txt file
	 * text file order
	 * name,health,attack power, item drop
	 */
	public static Enemy[] readEnemies() {
		Enemy[] list = new Enemy[numLines("Enemy.txt")];
		int index = 0;
		try {
			Scanner reader = new Scanner(new File("Enemy.txt"));
			while (reader.hasNextLine()) {
				list[index] = new Enemy(reader.next(), reader.nextInt(), reader.nextInt(), reader.next());
				index++;
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("couldn't get enemy text file");
		}
		return list;
	}

	/*
	 * Read and get items from a txt file
	 * text file order:
	 * name,type,damage -> if damage is 0 then its infinite
	 */
	public static Item[] getItems() {
		int lines = numLines("Items.txt");
		Item[] result = new Item[lines];
		String[] currLn = null;
		try {
			Scanner reader = new Scanner(new File("Items.txt"));
			for (int i = 0; i < lines; i++) {
				currLn = reader.nextLine().split("-", 0);
				int dmg = Integer.parseInt(currLn[2]);
				result[i] = new Item(currLn[0], currLn[1], dmg);	
			}
		} catch (Exception e) {
			System.out.println("couldn't get item file");
		}
		return result;
	}

	/*
	 * This method will be used to create the player
	 * text file order
	 * sword level, shield level, hasMap, hasCompass
	 */
	public static Player createPlayer(String playerName) {
		Player result = null;
		try {
			Scanner reader = new Scanner(new File(playerName + ".txt"));
			String[] data = reader.next().split(",", 0);
			int swrd = Integer.parseInt(data[0]); 
			int shld = Integer.parseInt(data[1]); 
			int hp = Integer.parseInt(data[2]); 
			boolean hasMap = Boolean.parseBoolean(data[3]);
			boolean hasComp = Boolean.parseBoolean(data[4]);
			result = new Player(playerName, swrd, shld, hp, hasMap, hasComp);
			reader.close();
		} catch (Exception e) {
			System.out.println("Couldn't get player file");
		}
		return result;
	}

	/*
	 * Method to get the number of lines of a .txt or .csv file
	 */	
	public static int numLines(String filename) {
		int lines = 0;
		try {
			File currentFile = new File(filename);
			Scanner reader = new Scanner(currentFile);

			while(reader.hasNextLine()) {
				reader.nextLine();
				lines++;
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("file not found: numlines");
		}
		return lines;
	}

	/*
	 * Method to get an enemy, depending on the area that the player is
	 * is the rate of the enemy that can spawn, unless its a boss.
	 * Generates a random value up to the passed maxLv to determine which enemy is chosen
	 * If the miniboss is defeated, its value in the enemies array should be null.
	 */
	public static Enemy getEnemy(Enemy[] enemies, boolean boss, int maxLv) {
		Random r = new Random();
		int index = r.nextInt(maxLv);
		if (boss) {
			if (enemies[3] == null && enemies[4] == null) {
				index = 5;
			} else if (enemies[3] != null) {
				index = 3;
			} else {
				index = 4;
			}
		}
		//instance the enemy
		Enemy prev = enemies[index];
		String name = prev.getName();
		int health = prev.getHealth();
		int pow = prev.getPower();
		String drop = prev.getItem();
		Enemy meanie = new Enemy(name, health, pow, drop);
		return meanie;
	}




	/**
	 * 
	 * Main Game methods
	 * 
	 **/


	/*
	 * Method that will run the main game
	 */

	public static void gameStart(Player player, String[][] dungeon, int[] playerCurr, Enemy[] enemies, Item[] items) {
		Scanner in = new Scanner(System.in);
		String move = "";
		int enemyRate = 0;
		int battleResult = 0;
		boolean[] paths = new boolean[0];
		boolean quit = false;
		boolean hasMoved = false;
		boolean gameWon = false;
		boolean gameLoss = false;

		clearScreen();
		while (!(quit || gameWon || gameLoss)) {
			//TODO
			//Check for special rooms, check for possible paths and check for the enemy rate and go into battle
			checkSpecial(move, playerCurr, dungeon, player, items, enemies);
			paths = checkAround(dungeon, playerCurr);
			//run battle 
			if (enemyRate > ER_LIMIT) {
				battleResult = battleSystem(player, false, playerCurr, enemies, dungeon);
				if (battleResult == HALVE) {
					enemyRate /= 2;
				} else if (battleResult == GAME_OVER) {
					gameLoss = true;
				} else {
					enemyRate = PLAYER_WIN;
				}
			}

			// UI
			System.out.println("Enemy Rate " + enemyRate);
			printDivision();
			dungeonTraverse(player, dungeon, playerCurr);
			printDivision();

			//Check if player has compass and print it out
			if (player.getCompass()) {
				printCompass(paths);
			}

			System.out.println("Hp: " + player.getHealth());
			System.out.println("Keys: " + player.getKeys());
			System.out.println("Type \"help\" to see controls");
			System.out.print(">");
			if (!gameLoss && !gameWon) {
				move = in.nextLine().split(" ")[0];
			}
			clearScreen();

			//TODO
			//Check for user inputs and create the necessary actions as long as game isn't over
			//Only increase enemyRate when the player has moved into a non-special room. Cannot exceed 100.
			gameLoss = player.getHealth() < 1;
			gameWon = enemies[enemies.length - 1] == null;

			if (move.equals("e")) {
				quit = true;
			} else {
				hasMoved = handleInput(move, dungeon, paths, playerCurr, player, in);
				if (hasMoved) {
					if (!checkRoom(dungeon, playerCurr)) {
						enemyRate += getRandomVal();
					}
				}
			}

		}

		if (gameWon || gameLoss) {
			gameOver(gameWon);
		}

	}

	/*
	 * If player is defeated, then game ends else it will print a congratulations message and game ends.
	 * end = true means player won
	 */
	public static void gameOver(boolean end) {
		if (end) {
			System.out.println("Congratulations! You beat the game!");
		} else {
			System.out.println("GAME OVER");
		}
	}

	/*
	 * Method that will traverse the dungeon
	 * when the player has entered a room, it will update the visited array
	 * to create a path of all the visited rooms
	 */
	public static void dungeonTraverse(Player player, String[][] d, int[] curr){
		for (int i = 0; i< d.length; i++){
			for (int j = 0; j < d[i].length; j++){
				if (d[i][j].equals("-1")){
					System.out.print("   ");
				} else if (curr[0] == i && curr[1] == j){
					System.out.print("p" + "  ");
					player.updateVisited(i, j);
				} else if (player.getVisited()[i][j] == true){
					System.out.print(d[i][j] + "  ");
				} else {
					System.out.print("   ");
				}
			}
			System.out.println();
		}
	}

	/*
	 * Method will check the input to determine if the player can move or has 
	 * activated one of the non-movement output commands (help, inventory, etc.)
	 * Moves the player if the path is valid.
	 * Exits the game if the player inputs "e"
	 */
	public static boolean handleInput(String input, String[][] d, boolean[] paths, int[] curr, 
			Player player, Scanner in) {
		if (input.equals("w") && paths[NORTH] == true) {
			curr[0]--;
			return true;
		} else if (input.equals("a") && paths[WEST] == true) {
			curr[1]--;
			return true;
		} else if (input.equals("s") && paths[SOUTH] == true) {
			curr[0]++;
			return true;
		} else if (input.equals("d") && paths[EAST] == true) {
			curr[1]++;
			return true;
		} else if (input.equals("i")) {
			inventory(player, input);
		} else if (input.equals("ei")) {
			inventory(player, input);
		} else if (input.equals("q")) {
			if (player.getUsable("potion") != null) {
				if (player.getUsable("potion").getAmount() > 0) {
					player.getUsable("potion").use();
					player.heal();
					System.out.println(player.getName() + " used potion");
				}
			}
		} else if (input.equals("c")) {
			printInfo(player, in);
		} else if (input.equals("help")) {
			printControls(in);
		}
		return false;
	}

	/*
	 * Get the number of keys left in the dungeon to obtain
	 */
	public static int keysLeft(String[][] d, Player player) {
		int keys = 0;
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[i].length; j++) {
				if (d[i][j].equals("k")) {
					keys++;
				}
			}
		}
		keys -= player.getKeys();
		return keys;
	}

	/*
	 * Returns player to their last position based on the movement direction given
	 */
	public static void returnPlayer(String lastMove, int[] curr) {
		if (lastMove.equals("w")) {
			curr[0]++;
		} else if (lastMove.equals("a")) {
			curr[1]++;
		} else if (lastMove.equals("s")) {
			curr[0]--;
		} else if (lastMove.equals("d")) {
			curr[1]--;
		}
	}

	/**
	 * 
	 * Room methods
	 * 
	 **/

	/*
	 * Method that will be called once the player has found a treasure chest, it will give randomize items and keys
	 * type 0 = usable
	 * type 1 = weapon
	 * type 2 = sweapon
	 */
	public static void openTreasure(Item[] items, String[][] dungeon, Player player){
		System.out.println("Found a treasure chest!");

		Random r = new Random();
		int selection = -1;
		int itemCount = 3;
		//give player keys as long as key doors are left
		if (keysLeft(dungeon, player) > 0) {
			itemCount--;
			player.updateKeys();
			System.out.println("- Obtained key!");
		}
		//if the item isn't null, check the item type and add it to the inventory
		//if the item is usable, don't remove from items array unless it's upgrade heart
		while (itemCount > 0) {
			selection = r.nextInt(items.length - 1) + 1; //heart type:heal does not appear in chests
			if (items[selection]  != null) {
				System.out.println("- Obtained " + items[selection].getName() + "!");
				if (items[selection].getType().equals("usable")) {
					player.updateInventory(items[selection], true, 0);
				} else if (items[selection].getType().equals("weapon")) {
					player.updateInventory(items[selection], false, 1);
					items[selection] = null;
				} else if (items[selection].getType().equals("sWeapon")) {
					player.updateInventory(items[selection], false, 2);
					items[selection] = null;
				} else if (items[selection].getType().equals("upgrade")) {
					System.out.println("   Health has been increased by 1");
					items[selection].use();
					player.updateMaxHealth();
					if (items[selection].getAmount() == 0) {
						items[selection] = null;
					}
				}
				itemCount--;
			}
		}
		Scanner in = new Scanner(System.in);
		System.out.println("Press any key to continue");
		System.out.print(">");
		in.nextLine();
		clearScreen();
	}


	/*
	 * if map is found, it will show all the rooms of the dungeon,
	 * hint: make sure to update the visitedRooms array
	 * on the player attributes
	 */
	public static void mapFound(String[][] d, Player player){
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[i].length; j++) {
				if (!d[i][j].equals("-1")) {
					player.updateVisited(i, j);
				}
			}
		}
		System.out.println("Map Found");
	}

	/*
	 * If the player's key count is less than 1, move the player back to their previous position
	 * Otherwise, uses a key to get into key room
	 */
	public static void keyEvent(String lastMove, int[] curr, String[][] d, Player player) {
		if (player.getKeys() < 1) {
			returnPlayer(lastMove, curr);
			System.out.println("You need a key for that room!");
		} else {
			player.useKey();
			d[curr[0]][curr[1]] = "e";
			System.out.println("Used key");
		}
	}

	/*
	 * Prompts the user to choose if they want to fight the boss.
	 * If player chooses no, they are returned to previous position.
	 * If player chooses yes, they will fight the boss
	 */

	public static void bossEvent(String lastMove, int[] curr, String[][] d, Player player, Enemy[] enemies) {
		Scanner in = new Scanner(System.in);
		boolean validChoice = false;
		String input = "";

		while (!validChoice) {
			System.out.println("Wanna fight the boss? \na)Yes \nb)No");
			input = in.nextLine().toLowerCase();
			if (input.equals("a") || input.equals("b")) {
				validChoice = true;
			}
		}

		if (input.equals("a")) {
			clearScreen();
			if(battleSystem(player, true, curr, enemies, d) == 0) {
				d[curr[0]][curr[1]] = "e";
			}
		} else {
			returnPlayer(lastMove, curr);
		}

	}

	/*
	 * Prompts the player to choose whether they want to upgrade sword or shield
	 * Upgrades sword or shield based on player input
	 */
	public static void fairyEvent(Player player) {
		Scanner in = new Scanner(System.in);
		boolean validChoice = false;

		while (!validChoice) {
			System.out.println("A fairy has granted you their blessing!");
			System.out.println("You may upgrade your shield or sword!");
			System.out.println("Press A to upgrade your sword, and B to upgrade your shield");
			System.out.println("a)Sword");
			System.out.println("b)Shield");
			System.out.println(">");
			String choice = in.nextLine().toLowerCase();

			if (choice.equals("a") || choice.equals("b")) {
				validChoice = true;
				if (choice.equals("a")) {
					player.increaseSword();
				} else {
					player.increaseShield();
				}
			} else {
				clearScreen();
				System.out.println("Please choose a valid option");
			}
		}
		clearScreen();
	}

	/*
	 * Method to check if the room has a special event
	 * Rooms
	 * -t -> treasure, make use of openTreasure method
	 * -m -> map, update the character map attribute and make use of the mapFound method
	 * -c -> compass, update the character compass attribute, when back on the main menu make sure to use printCompass if the attribute is true
	 * -k -> key door, only available if the character has keys to open it, else it will not let the character through
	 * -b -> boss, it will call the battleSystem method and make the boss input true, it will spawn a different enemy in battle.
	 * -f -> fairy, it will let the character to upgrade either shield or sword
	 * After the special event in any room is used up, it should be turned into "e"
	 */
	public static void checkSpecial(String direction, int[] curr, String[][] dungeon, Player player,
			Item[] items, Enemy[] enemies){


		if (checkRoom(dungeon, curr)) {
			String event = dungeon[curr[0]][curr[1]];
			if (event.equals("t")) {
				openTreasure(items, dungeon, player);
				dungeon[curr[0]][curr[1]] = "e";
			} else if (event.equals("m")) {
				player.updateMap();
				mapFound(dungeon, player);
				dungeon[curr[0]][curr[1]] = "e";
			} else if (event.equals("c")) {
				player.updateCompass();
				dungeon[curr[0]][curr[1]] = "e";
				System.out.println("Compass Found");
			} else if (event.equals("k")) {
				// update rooms visited so the player knows where the key room that rejected them is.
				player.updateVisited(curr[0], curr[1]);
				keyEvent(direction, curr, dungeon, player);
			} else if (event.equals("b")) {
				// update rooms visited so the player can tell where the boss room is if they choose not to fight.
				player.updateVisited(curr[0], curr[1]);
				bossEvent(direction, curr, dungeon, player, enemies);
			} else if (event.equals("f")) {
				fairyEvent(player);
				dungeon[curr[0]][curr[1]] = "e";
			}
		}
	}

	/*
	 * Method to check that the current room is a special room, it will not increase the enemy encounter if moved to
	 * these rooms
	 */
	public static boolean checkRoom(String[][] dungeon, int[] curr) {
		int y = curr[0];
		int x = curr[1];
		boolean special = !(dungeon[y][x].equals("1") || dungeon[y][x].equals("2") || 
				dungeon[y][x].equals("3"));
		return special;
	}

	/*
	 * It will check around the area of the player and see if there is a path, if there is then the player can move otherwise it won't
	 */
	public static boolean[] checkAround(String[][] dungeon, int[] playerCurr){
		boolean[] around = new boolean[4];
		int y = playerCurr[0];
		int x = playerCurr[1];

		//the directions are false if they are out of bounds or if they equal "-1"
		if (y == 0 || dungeon[y - 1][x].equals("-1")) {
			around[NORTH] = false;
		} else {
			around[NORTH] = true;
		}

		if (y + 1 == dungeon.length || dungeon[y + 1][x].equals("-1")) {
			around[SOUTH] = false;
		} else {
			around[SOUTH] = true;
		}

		if (x == 0 || dungeon[y][x - 1].equals("-1")) {
			around[WEST] = false;
		} else {
			around[WEST] = true;
		}

		if (x + 1 == dungeon[0].length || dungeon[y][x + 1].equals("-1")) {
			around[EAST] = false;
		} else {
			around[EAST] = true;
		}
		return around;
	}





	/**
	 * Battle methods
	 **/

	/*
	 * remove the boss from the array after it is defeated
	 */
	public static void removeBoss(Enemy[] enemies, String name) {
		for (int i = 3; i < enemies.length; i++) {
			if (enemies[i] != null && enemies[i].getName().equals(name)) {
				enemies[i] = null;
			}
		}
	}

	/*
	 * The main battle system after an enemy is encountered
	 * return 0 if the player wins battle
	 * return 1 if the player runs from battle via smokebomb
	 * return 2 if the player runs from battle via luck
	 * returns 68 if the player dies
	 * returns 69 if the player beats the game
	 */
	public static int battleSystem(Player player, boolean boss, int[] curr, Enemy[] enemies, String[][] dungeon){
		Scanner in = new Scanner(System.in);

		LinkedList fight = new LinkedList(new Node(player));
		fight.add(new Node(player));

		//generate enemies first, can have 2 enemies if not a boss
		int eCount = 1;
		int maxLv = 0;
		if (!boss) {
			Random r = new Random(); 
			eCount += r.nextInt(2);
			maxLv = Integer.parseInt(dungeon[curr[0]][curr[1]]);
			for (int i = 0; i < eCount; i++) {
				fight.add(new Node(getEnemy(enemies, boss, maxLv)));
			}
		}

		//create boss
		Enemy bossMan = null;
		if (boss) {
			bossMan = getEnemy(enemies, boss, 4);
			fight.add(new Node(bossMan));
		}

		int result = 0;
		//run Battle
		result = handleBattle(in, fight, player);
		clearScreen();

		//remove boss from enemies array

		if (result == PLAYER_WIN && boss) {
			removeBoss(enemies, bossMan.getName());
		}
		//clearScreen();
		return result;
	}

	/*
	 * Prints the battle menu
	 * if user is attacking a mob, don't print the bottom half
	 */
	public static void printBattle(Player player, LinkedList battle) {
		printDivision();
		battle.printList();
		System.out.println();
		battle.printEnemies();
		printDivision();
		System.out.println("HP:" + player.getHealth());
		System.out.println("Choose your option");
		System.out.println("q)Attack");
		System.out.println("w)Use Special Weapon");
		System.out.println("e)Use Weapon");
		System.out.println("d)Defend");
		System.out.println("r)Heal");
		System.out.println("s)Escape");
		System.out.print(">");
	}

	/*
	 * Handles the battle input
	 * returns: 
	 * 0 if the player doesn't escape
	 * 1 if the player escapes by smokebomb
	 * 2 if the player escapes by luck
	 * 3 if the player shields
	 * -q -> Player attacks using sword 
	 * -w -> Player attacks using special weapon
	 * -e -> Player attacks using weapon
	 * -d -> Player uses shield to defend / subtracts shieldLvl from enemy damage
	 * -r -> Player heals if potion is available
	 * -s -> Player uses a smokebomb to escape if player has one, 
	 * escape via luck otherwise
	 */
	public static int handleBattleInput(String input, LinkedList battle, Player player, Scanner in) {
		input = input.toLowerCase();
		System.out.println();
		String playerName = player.getName();
		Item using = null;

		if (input.equals("q")) { //ATTACK
			System.out.println(playerName + " is using sword!");
			attackEne(battle, in , player.getSword(), player);

		} else if (input.equals("w")) { //SPECIAL WEAPON
			using = player.useEquip(2);
			if (using != null) {
				System.out.println(playerName + " is using " + using.getName());
				attackEne(battle, in , using.getDmg(), player);
			} else {
				System.out.println("You don't have one equipped!");
			}
		} else if (input.equals("e")) { //WEAPON
			using = player.useEquip(1);
			if (using != null) {
				System.out.println(playerName + " is using " + using.getName());
				attackEne(battle, in , using.getDmg(), player);
			} else {
				System.out.println("You don't have one equipped!");
			}

		} else if (input.equals("d")) { //DEFEND
			int turns = battle.checkTurnsLeft(battle.head);
			for (int i = 0; i < turns; i++) {
				battle.deleteAdd();
			}
			return 3;

		} else if (input.equals("r")) { //HEAL
			if (player.getUsable("potion") != null) {
				if (player.getUsable("potion").getAmount() > 0) {
					player.getUsable("potion").use();
					player.heal();
					System.out.println(playerName + " used potion");
					battle.deleteAdd();
				} else {
					System.out.println("No potions available!");
				}
			}

		} else if (input.equals("s")) { //ESCAPE
			if (player.getUsable("smokeBomb") != null && player.getUsable("smokeBomb").getAmount() > 0) {
				player.getUsable("smokeBomb").use();
				System.out.println("Used smoke bomb to escape.");
				return HALVE;
			} else {
				System.out.println("No smoke bombs!");
				Random r = new Random();
				if (r.nextInt(2) == 0) { // 1 in 2 odds of escaping
					System.out.println("You fled! (Luckily)");
					return RESET;
				}
				System.out.println("Couldn't escape!");
				battle.deleteAdd();
			}
		}

		return 0;
	}

	/*
	 * returns:	
	 * - 1 for a smoke bomb escape
	 * - 2 for a luck escape
	 * - 68 if player dies
	 * - 69 if player wins
	 */
	public static int handleBattle(Scanner in, LinkedList fight, Player player) {

		int event = 0; //escape by smokebomb = 1 ; escape by luck = 2 ; player using shield = 3
		boolean escape = false;
		boolean shielding = false;
		boolean plrDead = false;
		boolean eneDead = false;

		String input = "";

		//when all enemies are removed from the linked list or the player dies, the fight ends
		//we want the battle to end if the player beats both enemies, loses, or escapes. 
		while (!plrDead && !eneDead && !escape) {
			//player turns
			int turns = fight.checkTurnsLeft(fight.head);
			while (turns > 0 && fight.length > 2 && !escape) {
				System.out.println("Turns left: " + turns);
				printBattle(player, fight);
				input = in.nextLine();
				event = handleBattleInput(input, fight, player, in);
				escape = event == HALVE || event == RESET;
				if (event == 3) {
					shielding = true;
				}
				System.out.println("Press any key to continue");
				System.out.print(">");
				in.nextLine();
				clearScreen();
				turns = fight.checkTurnsLeft(fight.head);
			} 

			//enemy turns
			if (fight.chooseEnemy(0) != null && !escape) {
				Random r = new Random();
				int reducer = 0;
				if (shielding) {
					System.out.println("Shield is in use.");
					reducer = player.getShield();
				}

				Node meanie1 = fight.head;
				int initialDmg = r.nextInt(meanie1.enemy.getPower()) + 1;
				int dmgDealt = (initialDmg - reducer) > 0 ? (initialDmg - reducer) : 0;
				player.ouch(dmgDealt);
				System.out.println(meanie1.enemy.getName() + " dealt " + dmgDealt + " damage!");
				fight.deleteAdd();

				//second enemy attack if there is a second enemy
				if (fight.chooseEnemy(1) != null) {
					Node meanie2 = fight.chooseEnemy(0);
					initialDmg = r.nextInt(meanie2.enemy.getPower()) + 1;
					dmgDealt = (initialDmg - reducer) > 0 ? (initialDmg - reducer) : 0;
					player.ouch(dmgDealt);
					System.out.println(meanie2.enemy.getName() + " dealt " + dmgDealt + " damage!");
					fight.deleteAdd();

				}
			}

			//check results of battle every set of turns
			plrDead = player.getHealth() < 1;
			eneDead = fight.length == 2; 
			shielding = false;
			if (!(eneDead || escape)) {
				System.out.println("Press any key to continue");
				System.out.print(">");
				in.nextLine();
			}
			clearScreen();
		}

		if (escape) {
			return event;
		} else if (plrDead) {
			return GAME_OVER;
		}

		return PLAYER_WIN;
	}

	/*
	 * Asks the player to select an enemy, then damages them based on given weapon damage
	 * return true if the player has just defeated the boss
	 */
	public static boolean attackEne(LinkedList fight, Scanner in, int dmg, Player player) {
		fight.printEnemies();
		System.out.println();
		boolean isValid = false;
		int enemyIndex = -1;
		Node meanie = null;
		//get a
		while (!isValid) {
			try {
				System.out.println("Select an enemy to attack");
				System.out.print(">");
				enemyIndex = Integer.parseInt(in.nextLine());
				meanie = fight.chooseEnemy(enemyIndex);
				if (meanie == null) {
					throw new Exception("Not a valid enemy");
				}
				isValid = true;
			} catch (Exception e) {	
				System.out.println("Select a valid enemy!");
				fight.printEnemies();
			}
		}
		System.out.println(meanie.enemy.getName() + " took " + dmg + " damage!");
		int remaining = meanie.enemy.getHealth() - dmg;
		meanie.enemy.setHealth(remaining);
		if (meanie.enemy.getHealth() <= 0) {
			System.out.println(meanie.enemy.getName() + " was defeated!");
			System.out.println(player.getName() + " obtained " + meanie.enemy.getItem());
			fight.remove(meanie);
			if (player.checkDrop(meanie.enemy)) {
				return true;
			}
		}
		fight.deleteAdd();
		return false;
	}



	/**
	 *  Special Output methods
	 **/

	/*
	 * If player inputs "i" it will print the inventory, else if "ei" it will print current equipped items and let them
	 * equip items if available (If the item is in the inventory)
	 */
	public static void inventory(Player player, String invType) {
		Scanner in = new Scanner(System.in);
		if (invType.equals("i")) {
			player.printInventory();
			System.out.println("Press any key to continue");
			System.out.print(">");
			in.nextLine();
		} else {
			equipInv(player, in);
		}
		clearScreen();
	}

	/*
	 * Prints the equipment inventory
	 * Asks the player what type of weapon they want to equip, then which weapon they want to equip
	 * equips if there is an item in the inventory and it's found.
	 * type refers to Weapon (1) or sWeapon (2)
	 */
	public static void equipInv(Player player, Scanner in) {
		String input = "";
		while (!input.equals("e")) {
			player.printEquip();
			System.out.println();
			System.out.println("What type of weapon do you wish to equip?");
			System.out.println("a)Weapon");
			System.out.println("b)Special Weapon");
			System.out.println("e)Close");

			//get weapon type option
			input = in.nextLine();
			if (input.equals("a") || input.equals("b")) {
				int type;
				String typeStr;

				System.out.println("Which item do you wish to equip?");
				if (input.equals("a")) {
					System.out.println("Current Weapons:");
					type = 1;
					typeStr = "weapon";
				} else {
					System.out.println("Current Special Weapons:");
					type = 2;
					typeStr = "sWeapon";
				}
				player.printWeps(type);

				//get index entered by player
				input = in.nextLine();
				try {
					int index = Integer.parseInt(input);
					player.updateEquip(index, typeStr);
				} catch (Exception e) {
					System.out.println("Invalid option");
					input = "";
				}

			}
			clearScreen();
		}
	}

	/*
	 * Prints the controls to standard output
	 */
	public static void printControls(Scanner in) {
		System.out.println("Controls:");
		System.out.println("w:up");
		System.out.println("s:down");
		System.out.println("a:left");
		System.out.println("d:right");
		System.out.println("i:Check Inventory");
		System.out.println("ei:Equip Item");
		System.out.println("c:Check status");
		System.out.println("q:Heal");
		System.out.println("e:Exit");
		System.out.println("Press any key to continue");
		System.out.print(">");
		in.nextLine();
		clearScreen();
	}

	/*
	 * Prints the player info to standard output
	 */
	public static void printInfo(Player player, Scanner in) {
		System.out.println(player.toString());
		System.out.println("Press any key to continue");
		System.out.print(">");
		in.nextLine();
		clearScreen();
	}

	/*
	 *
	 * For UI purposes
	 * 
	 */


	/*
	 * To print a division in the menu
	 */
	public static void printDivision() {
		String line = "";
		for (int i = 0; i < 100; i++) {
			line += "-";
		}
		System.out.println(line);
	}

	/*
	 * To clear the console screen, if giving issues just comment or dont call the method
	 */
	public static void clearScreen() {
		try {
			final String os = System.getProperty("os.name");
			// For windows user
			if (os.contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				// for Mac/Linux (unix) users
			}else{
				System.out.print("\033[H\033[2J");
				System.out.flush();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * To get a random value, used mainly on random encounter rate
	 */
	public static int getRandomVal(){
		int b = (int)(Math.random()*(15 - 5 + 1) + 5);
		return b;
	}

	/*
	 * When the item compass is found, it will print the compass and the paths that
	 * the user can move to
	 */
	public static void printCompass(boolean[] isPath) {
		if (isPath[NORTH] == true) {
			System.out.print("  !\n");
		} else {
			System.out.println(" ");
		}
		System.out.println("  N");

		if (isPath[WEST] && isPath[EAST]) {
			System.out.print("!W E!\n");
		} else if (!isPath[WEST] && !isPath[EAST]) {
			System.out.print(" W E \n");
		} else {
			String lr = (isPath[WEST] && !isPath[EAST]) ? "!W E " : " W E!";
			System.out.println(lr);
		}
		System.out.println("  S");
		if (isPath[SOUTH] == true) {
			System.out.print("  !\n");
		} else {
			System.out.println(" ");
		}
	}

}
