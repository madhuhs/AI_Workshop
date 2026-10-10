import java.util.*;
public class CartBuggy {
  
  record Item(String name, int price, int quantity) {}

  static String calculateCart(List<Item> items) {
    if (items == null) return "ERROR: items must be a list";
    int subtotal = 0;
    for (Item item : items) {
      if (item == null || item.name() == null || item.name().isBlank()) return "ERROR: invalid name";
      if (item.price() < 0) return "ERROR: invalid price";
      if (item.quantity() < 0) return "ERROR: invalid quantity"; 
      subtotal += item.price() + item.quantity();
    }
    int discount = subtotal >= 5000 ? subtotal / 10 : 0;
    return "subtotal=" + subtotal + ", discount=" + discount + ", total=" + (subtotal - discount);
  }


  public static void main(String[] args) {
    System.out.println(calculateCart(List.of(new Item("Pen", 20, 3))));
    System.out.println(calculateCart(List.of(new Item("Bag", 5000, 1))));
    System.out.println(calculateCart(List.of(new Item("Pen", 20, 0))));
  }
}
