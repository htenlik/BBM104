public abstract class Item {
    protected String name;
    protected int barcode;
    protected double price;

    public Item(String name, int barcode, double price) {
        this.name = name;
        this.barcode = barcode;
        this.price = price;
    }

    public abstract String getDetails();
}

class Toy extends Item{
    private final String color;

    public Toy(String name, String color, int barcode, double price) {
        super(name, barcode, price);
        this.color = color;
    }

    @Override
    public String getDetails() {
        return "Color of the " + name + " is " + color + ". Its barcode is " + barcode + " and its price is " + price;
    }

}

class Book extends Item{
    private final String author;

    public Book(String name, String author, int barcode, double price) {
        super(name, barcode, price);
        this.author = author;
    }

    @Override
    public String getDetails() {
        return "Author of the " + name + " is " + author + ". Its barcode is " + barcode + " and its price is " + price;
    }
}

class Stationery extends Item{
    private final String kind;

    public Stationery(String name, String kind, int barcode, double price) {
        super(name, barcode, price);
        this.kind = kind;
    }

    @Override
    public String getDetails() {
        return "Kind of the " + name + " is " + kind + ". Its barcode is " + barcode + " and its price is " + price;
    }
}
