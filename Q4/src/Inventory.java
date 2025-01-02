import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Inventory<T extends Item> {
    private final Map<Integer, T> items = new LinkedHashMap<>();  // Maintains insertion order

    public void addItem(T item) {
        items.put(item.barcode, item);
    }

    public void removeItem(int barcode, StringBuilder sb) {
        if (items.containsKey(barcode)) {
            items.remove(barcode);
            sb.append("Item is removed.\n");
        } else {
            sb.append("Item is not found.\n");
        }
    }

    public void searchByBarcode(int barcode, StringBuilder sb) {
        T item = items.get(barcode);
        if (item != null) {
            sb.append(item.getDetails()).append("\n");
        } else {
            sb.append("Item is not found.\n");
        }
    }

    public void searchByName(String name, StringBuilder sb) {
        for (T item : items.values()) {
            if (item.name.equalsIgnoreCase(name)) {
                sb.append(item.getDetails()).append("\n");
                return;
            }
        }
        sb.append("Item is not found.\n");
    }

    public void displayItems(StringBuilder sb) {
        sb.append("INVENTORY:\n");
        List<T> books = new ArrayList<>();
        List<T> toys = new ArrayList<>();
        List<T> stationeries = new ArrayList<>();

        // Sort items into categories while maintaining their addition order
        for (T item : items.values()) {
            if (item instanceof Book) {
                books.add(item);
            } else if (item instanceof Toy) {
                toys.add(item);
            } else if (item instanceof Stationery) {
                stationeries.add(item);
            }
        }

        // Display each category in the order added
        books.forEach(book -> sb.append(book.getDetails()).append("\n"));
        toys.forEach(toy -> sb.append(toy.getDetails()).append("\n"));
        stationeries.forEach(stationery -> sb.append(stationery.getDetails()).append("\n"));
    }
}
