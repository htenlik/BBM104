/**
 * This class is used to simulate the operation of a Gym Meal Machine. It includes methods for displaying the machine status and handling the main process.
 */
public class Main {

    /**
     * This method is used to display the status of the machine. For each product, its name, calories, and available quantity are shown.
     *
     * @param product The product whose machine status is to be displayed. It is not modified by this method.
     * @return A string representing the status of the machine. It contains product names, their calorie content, and available quantity.
     */

    private static String displayMachineStatus(Product product) {
        String[][] machine = product.getMachine();
        int[][] slots = product.getSlots();

        StringBuilder sb = new StringBuilder();
        sb.append("-----Gym Meal Machine-----\n");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                String itemName = machine[i][j] != null ? machine[i][j] : "___";
                int calorie = product.getCalorie()[i][j];
                int slotCount = slots[i][j];
                sb.append(itemName).append("(").append(calorie).append(", ").append(slotCount).append(")___");
            }
            sb.append("\n");
        }
        sb.append("----------");
        return sb.toString();

    }

    /**
     * The main method where the application starts. It reads product and purchase lists from files, displays the machine status before and after purchases, and writes outputs to a file.
     *
     *
     * @param args Command line arguments used for specifying input and output files. The first argument is the product list, the second is the purchase list, and the third is the output file.
     */
    public static void main(String[] args) {

        String[] productList = FileInput.readFile(args[0], false, false); // The file is read as it is without discarding or trimming anything, and the content is stored in a string array.
        String[] purchaseList = FileInput.readFile(args[1], false, false); // The file is read as it is without discarding or trimming anything, and the content is stored in a string array.

        Product product = new Product();
        Purchase purchase = new Purchase(product);

        String output1 = product.takeInput(productList);

        String output2 = displayMachineStatus(product);

        String output3 = purchase.takeInput(purchaseList);

        String output4 = displayMachineStatus(product);

        FileOutput.writeToFile(args[2], output1, false, false);
        FileOutput.writeToFile(args[2], output2, true, true);
        FileOutput.writeToFile(args[2], output3, true, false);
        FileOutput.writeToFile(args[2], output4, true, false);

    }

}
