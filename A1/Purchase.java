/**
 * This class is responsible for handling the purchasing process of products from the Gym Meal Machine.
 * It keeps track of the selected product and its price.
 */
public class Purchase {
    // Variables.
    private final Product product;
    private String purchased;
    private int prices;

    // Getters.
    public String getPurchased() {
        return purchased;
    }

    public int getPrices() {
        return prices;
    }

    public Purchase(Product product) {
        this.product = product;
    }

    /**
     * This method is used to process the input from the user and handle the purchase of products.
     *
     * @param inputFile2 An array of strings representing the user input.
     * @return A string containing the output of the purchase process.
     */
    public String takeInput(String[] inputFile2) {
        String inputFile = String.join("\n", inputFile2); // Convert array to string
        String[] lines = inputFile.split("\\n"); // Split using regex for newline

        StringBuilder output = new StringBuilder(); // Declare output variable outside the loop

        for (String line : lines) {
            String[] fields = line.split("\\t"); // Split using regex for tab

            String choice = fields[2];
            int value = Integer.parseInt(fields[3]);
            String[] money = fields[1].split(" ");
            int budget = 0;
            for (String str : money) {
                budget += Integer.parseInt(str);
            }

            output.append(sell(line, budget, choice, value));
        }

        return output.toString();
    }

    /**
     * This method checks if the budget is enough to afford the product with the given price.
     *
     * @param budget1 The budget available for the purchase.
     * @param price1 The price of the product.
     * @return A boolean indicating if the product can be afforded.
     */

    private boolean isAffordTo(int budget1, int price1){
        return budget1 > price1;
    }

    /**
     * This method finds the product choice based on the criteria provided by the user and updates the purchase details.
     *
     * @param choice The type of product or the selection method chosen by the user.
     * @param value The value associated with the choice, like nutrition value or slot number.
     * @param budget The budget available for the purchase.
     */

    private int findChoice(String choice, int value, int budget) {

        int[][] slots = product.getSlots();
        int[][] carb = product.getCarb();
        int[][] protein = product.getProtein();
        int[][] fat = product.getFat();
        int[][] calorie = product.getCalorie();
        int[][] price = product.getPrice();
        String[][] machine = product.getMachine();

        switch (choice) {
            case "PROTEIN":
                findForNutrition(protein, value, budget, slots, machine, calorie, price, carb, protein, fat);
                break;

            case "CARB":
                findForNutrition(carb, value, budget, slots, machine, calorie, price, carb, protein, fat);
                break;

            case "FAT":
                findForNutrition(fat, value, budget, slots, machine, calorie, price, carb, protein, fat);
                break;

            case "CALORIE":
                findForNutrition(calorie, value, budget, slots, machine, calorie, price, carb, protein, fat);
                break;


            case "NUMBER":
                if (0 <= value && value < 24) {
                    int i = value / 4;
                    int j = value % 4;
                    if (machine[i][j] == null) {
                        purchased = "slotIsEmpty";
                    }
                    else{
                        if (isAffordTo(budget, price[i][j])) {
                            purchased = machine[i][j];
                            slots[i][j]--;
                            prices = price[i][j];
                        }
                        else {
                            purchased = "cantAfford";
                            return -1;
                        }
                    }
                } else {
                    purchased = "numberIndexError";
                    return -1;
                }
                return -1;
            default:
                purchased = "productNotFound";
                return -1;

        }
        return value;
    }
    /**
     * This method is used to find a product based on nutritional values within a specified range and updates the purchase details.
     *
     * @param nutrition The nutritional information of the products.
     * @param value The nutritional value specified by the user.
     * @param budget The budget available for the purchase.
     * @param slots The availability of product slots in the machine.
     * @param machine The products available in the machine.
     * @param calorie The calorie information of the products.
     * @param price The price of the products.
     * @param carb The carbohydrate information of the products.
     * @param protein The protein information of the products.
     * @param fat The fat information of the products.
     */
    private int findForNutrition(int[][] nutrition, int value, int budget, int[][] slots, String[][] machine, int[][] calorie, int[][] price,int[][] carb,int[][] protein,int[][] fat ) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (nutrition[i][j] >= (value - 5) && nutrition[i][j] <= (value + 5)) {
                    if (slots[i][j] == 0) {
                        clear(machine, calorie, protein, fat, carb, price, i, j);
                        continue;
                    }
                    if (isAffordTo(budget, price[i][j])) {
                        purchased = machine[i][j];
                        slots[i][j]--;
                        prices = price[i][j];
                    } else {
                        purchased = "cantAfford";
                        return -1;
                    }
                    return -1;
                }
            }
        }
        purchased = "productNotFound";
        return -1;
    }
    /**
     * This method clears the information of a product from the machine.
     *
     * @param machine The products available in the machine.
     * @param calorie The calorie information of the products.
     * @param protein The protein information of the products.
     * @param fat The fat information of the products.
     * @param carb The carbohydrate information of the products.
     * @param price The price of the products.
     * @param i The row index of the product in the machine.
     * @param j The column index of the product in the machine.
     */
    private void clear(String[][] machine, int[][] calorie, int[][] protein, int[][] fat, int[][] carb, int[][] price, int i, int j){
        machine[i][j] = null;
        calorie[i][j] = 0;
        protein[i][j] = 0;
        fat[i][j] = 0;
        carb[i][j] = 0;
        price[i][j] = 0;

    }

    private String sell(String line, int budget, String choice, int value) {

        findChoice(choice, value,budget);
        int priceProduct = getPrices();

        String output = "";
        output += "INPUT: " + line + "\n";


        switch (purchased) {
            case "numberIndexError":
                output += "INFO: Number cannot be accepted. Please try again with another number.\n";
                output += "RETURN: Returning your change: " + budget + " TL\n";
                break;
            case "slotIsEmpty":
                output += "INFO: This slot is empty, your money will be returned.\n";
                output += "RETURN: Returning your change: " + budget + " TL\n";
                break;
            case "productNotFound":
                output += "INFO: Product not found, your money will be returned.\n";
                output += "RETURN: Returning your change: " + budget + " TL\n";
                break;
            case "debug":
                output += "INFO: debug.\n";
                output += "RETURN: debug: " + budget + " TL\n";
                break;
            case "cantAfford":
                output += "INFO: Insufficient money, try again with more money.\n";
                output += "RETURN: Returning your change: " + budget + " TL\n";
                break;
            default:
                output += "PURCHASE: You have bought one " + getPurchased() + "\n";
                output += "RETURN: Returning your change: " + (budget - priceProduct) + " TL\n";
                break;
        }
        return output;
    }

    }

