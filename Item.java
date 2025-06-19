package processor;

public class Item implements Comparable<Item> {
	private String itemName;
	private double cost;
	private int quantity = 0;

	public Item(String itemName, double cost) {
		this.itemName = itemName;
		this.quantity = 1;
		this.cost = cost;
	}

	public String getItemName() {
		return itemName;
	}

	public double getCost() {
		return cost;
	}

	public int getQuantity() {
		return quantity;
	}

	public void addQuantity() {
		quantity += 1;
	}

	public int compareTo(Item item) {
		if (this.itemName.compareTo(item.getItemName()) < 0) {
			return -1;
		} else if (this.itemName.compareTo(item.getItemName()) == 0) {
			return 0;
		} else {
			return 1;
		}
	}

//	public String toString() {
//		return "Item's name: " + itemName + ", Cost per item: $" + cost + ", ";
//	}

}
