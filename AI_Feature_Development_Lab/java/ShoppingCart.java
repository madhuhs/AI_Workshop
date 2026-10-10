import java.util.*;

public class ShoppingCart {
    public static class Item {
        public final String name;
        public final int price;
        public final int quantity;
        public Item(String name, int price, int quantity) {
            this.name = name; this.price = price; this.quantity = quantity;
        }
    }
    public static Map<String, Long> calculate(List<Item> items) {
        if (items == null) throw new IllegalArgumentException("items required");
        long subtotal = 0;
        for (Item item : items) {
            if (item == null || item.name == null || item.name.trim().isEmpty() || item.price < 0 || item.quantity <= 0)
                throw new IllegalArgumentException("invalid item");
            subtotal += (long)item.price * item.quantity;
        }
        long discount = subtotal > 5000 ? subtotal / 10 : 0;
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("subtotal", subtotal);
        result.put("discount", discount);
        result.put("total", subtotal - discount);
        return result;
    }
    public static void main(String[] args) {
        System.out.println(calculate(Arrays.asList(new Item("Bag", 3000, 2))));
    }
}
