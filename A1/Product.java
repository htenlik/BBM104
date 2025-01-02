import java.lang.Math;

/**
 * The Product class is designed to manage the products in a Gym Meal Machine. It stores product information and handles the input of new products into the machine.
 */
public class Product {

    private final String[][] machine = new String[6][4];
    private final int[][] slots = new int[6][4];
    private final int[][] price = new int[6][4];
    private final int[][] carb = new int[6][4];
    private final int[][] protein = new int[6][4];
    private final int[][] fat = new int[6][4];
    private final int[][] calorie = new int[6][4];

    /**
     * This method is used for processing the input file and loading products into the machine. Each line of the input represents a product and its details.
     *
     * @param inputFile1 An array of strings, each representing a line from the input file.
     * @return A string that contains messages about the loading process, indicating whether products were successfully loaded or if the machine is full.
     */

    public String takeInput(String[] inputFile1) {
        String inputFile = String.join("\n", inputFile1);
        String[] lines = inputFile.split("\\n");
        StringBuilder output1 = new StringBuilder();
        for (String line : lines) {
            String[] fields = line.split("\\t");

            String name = fields[0];
            int price = Integer.parseInt(fields[1]);

            String info = fields[2];
            String[] food = info.split(" ");
            double protein = Double.parseDouble(food[0]);
            double carb = Double.parseDouble(food[1]);
            double fat = Double.parseDouble(food[2]);

            double calorie = carb * 4 + protein * 4 + fat * 9;

            int fillResult = fill(name, price, carb, protein, fat, calorie);
            if (fillResult == -2) {
                output1.append("INFO: There is no available place to put ").append(name).append("\n");
            } else if (fillResult == -1) {
                output1.append("INFO: There is no available place to put ").append(name).append("\n");
                output1.append("INFO: The machine is full!" + "\n");
                break;
            }


        }
        return output1.toString();
    }

    /**
     * Attempts to fill the machine with a product. It checks for available slots or existing slots with the same product that are not full.
     *
     * @param name The name of the product.
     * @param price The price of the product.
     * @param carb The carbohydrate content of the product.
     * @param protein The protein content of the product.
     * @param fat The fat content of the product.
     * @param calorie The total calorie count of the product, calculated from its macronutrients.
     * @return An integer indicating the result of the attempt: 0 for success, -1 if the machine is full, and -2 if there's no available place for the product.
     */

    private int fill(String name, int price, double carb, double protein, double fat, double calorie) {
        int row = 0;
        int col = 0;
        // Find the next available slot or slot with the same product
        boolean found = false;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (machine[i][j] == null || (machine[i][j].equals(name) && slots[i][j] != 10)) {
                    row = i;
                    col = j;
                    found = true;
                    break;
                }
            }
            if (found) {
                slots[row][col]++;

                this.price[row][col] = price;
                this.carb[row][col] = (int) Math.round(carb);
                this.protein[row][col] = (int) Math.round(protein);
                this.fat[row][col] = (int) Math.round(fat);
                this.calorie[row][col] = (int) Math.round(calorie);

                machine[row][col] = name;
                return 0;
            }
        }

        // Check if all slots are full
        boolean full = true;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (slots[i][j] < 10) {
                    full = false;
                    break;
                }
            }
        }
        if(!full){
            return -2;

        }
        else{
            return -1;
        }

    }

    // Getter and Setters
    public int[][] getPrice() {
        return price;
    }

    public int[][] getCarb() {
        return carb;
    }

    public int[][] getFat() {
        return fat;
    }

    public int[][] getCalorie() {
        return calorie;
    }

    public String[][] getMachine() {
        return machine;
    }

    public int[][] getSlots() {
        return slots;
    }

    public int[][] getProtein() {
        return protein;
    }

}
