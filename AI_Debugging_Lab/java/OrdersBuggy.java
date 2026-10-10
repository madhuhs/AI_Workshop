import java.util.*;
public class OrdersBuggy {
  record Order(int id, String customer, int amount, String status) {}
  static String analyzeOrders(List<Order> orders, int threshold) {
    if (threshold < 0) return "ERROR: invalid threshold";
    Map<String, Integer> spending = new LinkedHashMap<>();
    List<Integer> above = new ArrayList<>();
    for (Order order : orders) {
      if (!order.status().equals("COMPLETED")) continue;
      spending.put(order.customer(), order.amount()); // BUG O1
      if (order.amount() >= threshold) above.add(order.id()); // BUG O2
    }
    String top = null;
    for (String customer : spending.keySet()) {
      if (top == null || spending.get(customer) > spending.get(top)) top = customer; // BUG O3
    }
    return "spending=" + spending + ", topCustomer=" + top + ", ordersAboveThreshold=" + above;
  }
  public static void main(String[] args) {
    var orders = List.of(
      new Order(1, "Zara", 100, "COMPLETED"),
      new Order(2, "Zara", 200, "COMPLETED"),
      new Order(3, "Asha", 300, "COMPLETED"),
      new Order(4, "Ravi", 100, "CANCELLED")
    );
    System.out.println(analyzeOrders(orders, 200));
  }
}
