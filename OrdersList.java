package processor;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.TreeMap;

public class OrdersList implements Runnable {
	private TreeMap<String, Integer> itemsCount;
	private GrandTotal total;
	private Order orders;
	private String details;

	public OrdersList(Order orders, TreeMap<String, Integer> itemsCount, GrandTotal total) {
		this.itemsCount = itemsCount;
		this.total = total;
		this.orders = orders;
	}

	public String getOrderDetails() {
		return details;
	}

	@Override
	public void run() {
		details = "----- Order details for client with Id: " + orders.getId() + " -----\n";
		double totalOrder = 0;

		// simulating time takes to process an item
		try {
			Thread.sleep((int) (Math.random() * 300));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (Item item : orders) {
			String itemName = item.getItemName();
			String cost = NumberFormat.getCurrencyInstance().format(item.getCost());

			double costs = item.getQuantity() * item.getCost();
			details += "Item's name: " + itemName + ", Cost per item: " + cost + ", Quantity: " + item.getQuantity()
					+ ", Cost: " + NumberFormat.getCurrencyInstance().format(costs) + "\n";
			totalOrder += costs;
		}

		// keeping track of number of items count
		ArrayList<String> allItemsInOrder = orders.getAllItemsInOrder();
		for (String itemName : allItemsInOrder) {
			synchronized (itemsCount) {
				Integer number = itemsCount.get(itemName);
				if (number == null) {
					itemsCount.put(itemName, 1);
				} else {
					itemsCount.put(itemName, number + 1);
				}
			}
		}

		details += "Order Total: " + NumberFormat.getCurrencyInstance().format(totalOrder) + "\n";
//		System.out.println(details);

		synchronized (total) {
			total.add(totalOrder);
		}
	}
}