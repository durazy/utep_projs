/**
* Part of this class was provided by TAs (constructors and method headers)
* Most of methods were completed by @durazy
**/

public class LinkedList {
	// attributes
	public Node head;
	public int length = 1;
	public int enemies;

	// constructors
	public LinkedList() {
		this.head = null;
	}

	public LinkedList(Node head) {
		this.head = head;
	}

	public LinkedList(Node uno, int length) {
		this.head = uno;
		this.length = length;
	}

	/*
	 * add node to list
	 */
	public void add(Node node) {
		if (this.head == null){
			this.head = node;
			this.length++; 
		}else{
			Node temp = this.head;
			while (temp.next != null) {
				temp = temp.next;
			}
			temp.next = node;
			node.next = null;
			this.length++;
		}
	}

	/*
	 *  delete first node and add to end
	 *  make use of add()
	 */
	public void deleteAdd() {
		Node removed = this.head;
		this.head = this.head.next;
		add(removed);
		this.length--;
	}

	/*
	 * remove node from list
	 */
	public void remove(Node node) {
		boolean removed = true;
		if (node == head) {
			head = node.next;
		} else {
			Node temp = this.head;
			Node prev = null;
			while (temp.next != null && temp.next != node) {
				temp = temp.next;
			}
			prev = temp;
			if (prev.next == null) {
				//node not found, at end of list
				removed = false;
			} else if (prev.next.next == null) {
				prev.next = null;
			} else {
				prev.next = prev.next.next;
			}
		}

		if (removed) {
			this.length--;
		}
	}

	/*
	 * Print all the items in the list
	 */
	public void printList() {
		Node temp = this.head;
		for (int i = 1; i <= this.length; i++) {
			if (temp.player != null) {
				System.out.print(temp.player.getName());
			} else if (temp.enemy != null) {
				System.out.print(temp.enemy.getName());
			}
			if (i != this.length) {
				System.out.print("->");
			}
			temp = temp.next;
		}
		System.out.println();
	}

	/*
	 * Print all the enemies in the list
	 */
	public void printEnemies() {
		Node temp = this.head;
		int count = 0;
		for (int i = 1; i <= length; i++) {
			if (temp.enemy != null) {
				System.out.print(count + ") " + temp.enemy.getName() + "  ");
				count++;
			}
			temp = temp.next;
		}
		System.out.println();
	}

	/*
	 * Get the chosen enemy
	 */
	public Node chooseEnemy(int index) {
		Node temp = this.head;
		int count = 0;
		for (int i = 1; i <= length; i++) {
			if (temp.enemy != null) {
				if (count == index) {
					return temp;
				}
				count++;
			}
			temp = temp.next;
		}
		return null;
	}

	/*
	 * Check how many turns the player has, its done recursively
	 * Always going to be passed the head
	 */
	public int checkTurnsLeft(Node node) {
		if (node != null && node.enemy == null) {
				return 1 + checkTurnsLeft(node.next);
		}
		return 0;
	}
}
