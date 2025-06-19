package processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class Order implements Iterable<Item> {
	private int id;
	private ArrayList<Item> items;
	private ArrayList<String> allItemsInOrder;
	private String multThreads, resultFile;
	private boolean isMultThreads;

	public Order(int id, String multThreads, String resultFile) {
		this.id = id;
		items = new ArrayList<Item>();
		allItemsInOrder = new ArrayList<String>();
		this.multThreads = multThreads;
		this.resultFile = resultFile;
	}

	public int getId() {
		return id;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public String getResultFile() {
		return resultFile;
	}

	public void addItem(String itemName, TreeMap<String, Double> itemAndCosts) {
		boolean exist = false;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getItemName().equals(itemName)) {
				exist = true;
			}
		}
		Set<String> item = itemAndCosts.keySet();
		double cost = 0;
		for (String temp : item) {
			if (temp.equals(itemName)) {
				cost = itemAndCosts.get(temp);
			}
		}
		if (!exist) {
			items.add(new Item(itemName, cost));
		} else {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getItemName().equals(itemName)) {
					Item existItem = items.get(i);
					existItem.addQuantity();
				}
			}
		}
		Collections.sort(items);
	}

	public void addAllItem(String itemName) {
		allItemsInOrder.add(itemName);
	}

	public ArrayList<String> getAllItemsInOrder() {
		return allItemsInOrder;
	}

	public boolean isMultThreads() {
		if (multThreads.equals("y")) {
			isMultThreads = true;
		} else {
			isMultThreads = false;
		}
		return isMultThreads;
	}

	public Iterator<Item> iterator() {
		return items.iterator();
	}

	public String toString() {
		return Integer.toString(id);
	}

}
