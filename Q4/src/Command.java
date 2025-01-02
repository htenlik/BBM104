public class Command {

    public static void handleCommand(String line, Inventory<Item> inventory, StringBuilder sb) {
        String[] parts = line.split("\\t");
        int barcode; // Declare outside the switch to use in REMOVE and SEARCHBYBARCODE.
        switch (parts[0]) {
            case "ADD":
                addItem(parts, inventory);
                break;
            case "REMOVE":
                barcode = Integer.parseInt(parts[1]);
                sb.append("REMOVE RESULTS:\n");
                inventory.removeItem(barcode, sb);
                sb.append("------------------------------\n");
                break;
            case "SEARCHBYBARCODE":
                barcode = Integer.parseInt(parts[1]);
                sb.append("SEARCH RESULTS:\n");
                inventory.searchByBarcode(barcode, sb);
                sb.append("------------------------------\n");
                break;
            case "SEARCHBYNAME":
                String name = parts[1];
                sb.append("SEARCH RESULTS:\n");
                inventory.searchByName(name, sb);
                sb.append("------------------------------\n");
                break;
            case "DISPLAY":
                inventory.displayItems(sb);
                sb.append("------------------------------\n");
                break;
            default:
                sb.append("Unknown command: ").append(line);
        }
    }

    public static void addItem(String[] parts, Inventory<Item> inventory) {
        String type = parts[1];
        String name = parts[2];
        int barcode = Integer.parseInt(parts[4]);
        double price = Double.parseDouble(parts[5]);

        switch (type) {
            case "Book":
                String author = parts[3];
                Book book = new Book(name, author, barcode, price);
                inventory.addItem(book);
                break;
            case "Toy":
                String color = parts[3];
                Toy toy = new Toy(name, color, barcode, price);
                inventory.addItem(toy);
                break;
            case "Stationery":
                String kind = parts[3];
                Stationery stationery = new Stationery(name, kind, barcode, price);
                inventory.addItem(stationery);
                break;
        }
    }

}
