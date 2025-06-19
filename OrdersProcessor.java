package processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class OrdersProcessor {

	public static ArrayList<Order> readOrders() throws FileNotFoundException {
		ArrayList<Order> allOrders = new ArrayList<Order>();
		TreeMap<String, Double> itemAndCosts = new TreeMap<>();
		Scanner read = new Scanner(System.in);
		System.out.println("Enter item's data file name: ");
		String itemData = read.next();
		Scanner scanItemData = new Scanner(new File(itemData));
		while (scanItemData.hasNext()) {
			itemAndCosts.put(scanItemData.next(), scanItemData.nextDouble());
		}
		System.out.println("Enter 'y' for multiple threads, any other character otherwise: ");
		String multThreads = read.next();
		System.out.println("Enter number of orders to process: ");
		int numberOfOrders = read.nextInt();
		System.out.println("Enter order's base filename: ");
		String baseFile = read.next();
		System.out.println("Enter result's filename: ");
		String resultFile = read.next();

		long startTime = System.currentTimeMillis();
		for (int i = 1; i <= numberOfOrders; i++) {
			Scanner scanBaseFile = new Scanner(new File(baseFile + i + ".txt"));
			scanBaseFile.next(); // skips ClientID:
			int clientId = scanBaseFile.nextInt(); // scans id
			Order order = new Order(clientId, multThreads, resultFile); // creates new order under id
			System.out.println("Reading order for client with: " + clientId);
			while (scanBaseFile.hasNextLine()) {
				String itemName = scanBaseFile.next();
				order.addItem(itemName, itemAndCosts);
				order.addAllItem(itemName);
//				order.addItem(scanBaseFile.next(), itemAndCosts); // takes item name
				scanBaseFile.nextLine(); // skips date
			}
			allOrders.add(order);
			scanBaseFile.close();
		}
		long endTime = System.currentTimeMillis();

		System.out.println("Processing time (msec): " + (endTime - startTime));
		System.out.println("Results can be found in the file: " + resultFile);
		read.close();
		return allOrders;
	}

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		ArrayList<Order> allOrders = readOrders();
		TreeMap<String, Integer> itemsCount = new TreeMap<>();
		GrandTotal total = new GrandTotal(0);
		String resultFileName, orderDetails = "";
		ArrayList<OrdersList> allOrdersList = new ArrayList<>();

		if (allOrders.get(0).isMultThreads()) {
			ArrayList<Thread> allThreads = new ArrayList<>();
			for (Order order : allOrders) {
				OrdersList ordersList = new OrdersList(order, itemsCount, total);
				allThreads.add(new Thread(ordersList));
				allOrdersList.add(ordersList);
			}

			for (Thread thread : allThreads) {
				thread.start();
			}

			for (Thread thread : allThreads) {
				thread.join();
			}
			for (OrdersList order : allOrdersList) {
				orderDetails += order.getOrderDetails();
			}
		} else {
			for (Order order : allOrders) {
				OrdersList ordersList = new OrdersList(order, itemsCount, total);
				ordersList.run();
				orderDetails += ordersList.getOrderDetails();
			}
		}

		String summary = "***** Summary of all orders *****\n";
		Set<String> items = itemsCount.keySet();
		for (String item : items) {
			double itemCost = 0;
			for (Order singleOrder : allOrders) {
				for (Item temp : singleOrder) {
					if (temp.getItemName().equals(item)) {
						itemCost = temp.getCost();
					}
				}
			}
			summary += "Summary - Item's name: " + item + ", Cost per item: "
					+ NumberFormat.getCurrencyInstance().format(itemCost) + ", Number sold: " + itemsCount.get(item)
					+ ", Item's Total: " + NumberFormat.getCurrencyInstance().format(itemCost * itemsCount.get(item))
					+ "\n";
		}
		System.out.print(summary);
		String grandTotal = "Summary Grand Total: " + NumberFormat.getCurrencyInstance().format(total.getValue());
		System.out.println(grandTotal);

		try {
			resultFileName = allOrders.get(0).getResultFile();
			FileWriter makeFile = new FileWriter(resultFileName, false);
			makeFile.write(orderDetails + summary + grandTotal);
			makeFile.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
