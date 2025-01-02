public class Main {
    public static void main(String[] args) {

        ItemReader itemReader = new ItemReader();
        String[] items = FileInput.readFile(args[0], false, false);
        itemReader.itemInitialize(items);

        ConcreteBuilder cb = new ConcreteBuilder(itemReader);
        String[] decoration = FileInput.readFile(args[1], false, false);

        for (String item : decoration) {
            FileOutput.writeToFile(args[2],cb.Decorate(item),true,true);
        }

        // printing TotalCost
        FileOutput.writeToFile(args[2], ConcreteBuilder.printTotalCost(),true,false);


    }
}