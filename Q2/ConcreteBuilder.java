
import java.lang.Math;



public class ConcreteBuilder implements Builder {


    // To access info's from items.txt
    public ConcreteBuilder(ItemReader itemReader) {
        this.itemReader = itemReader;
    }

    private final ItemReader itemReader;
    private static int totalCost = 0;


    public String Decorate(String inputFile){
        StringBuilder output = new StringBuilder();

        String[] content = inputFile.split("\t");


        String className = content[0];
        int pointer1 = this.SetPointer(className);

        double wallArea = this.SetWallsArea(pointer1);
        double floorArea = this.SetFloorsArea(pointer1);


        String wallDecoration = content[1];
        int pointer2 = this.SetPointer(wallDecoration);
        String wallOutput = this.SetWalls(pointer2, wallArea);


        String FloorDecoration= content[2];
        int pointer3 = this.SetPointer(FloorDecoration);
        String floorOutput = this.SetFloors(pointer3, floorArea);


        int cost = this.SetPrices(pointer2,pointer3,wallArea,floorArea);
        totalCost += cost;

        output.append("Classroom ").append(className).append(" used ")
                .append(wallOutput).append("for walls and used ")
                .append(floorOutput).append("for flooring, these costed ")
                .append(cost).append("TL.");

        return output.toString();



    }
    public static String printTotalCost() {
       return ("Total price is: " + totalCost + "TL.");
    }

    @Override
    public String SetWalls(int pointer, double wallArea) {
        if(itemReader.getShapeOrType()[pointer].equals("Paint")){
            return (int)Math.ceil(wallArea)+"m2 "+ "of Paint ";
        }
        else if(itemReader.getShapeOrType()[pointer].equals("Wallpaper")){
            return (int)Math.ceil(wallArea)+"m2 "+ "of Wallpaper ";
        }
        else if(itemReader.getShapeOrType()[pointer].equals("Tile")){
            double piece = Math.ceil(wallArea / itemReader.getLengthOrArea()[pointer]);
            return (int)piece + " Tiles ";
        }
        return "";
    }

    @Override
    public String SetFloors(int pointer, double floorArea) {
        if(itemReader.getShapeOrType()[pointer].equals("Tile")){
            double piece = Math.ceil(floorArea / itemReader.getLengthOrArea()[pointer]);
            return (int)piece + " Tiles ";
        }
        else {
            return "ERROR";
        }
    }

    @Override
    public int SetPrices(int pointer2, int pointer3, double wallArea, double floorArea) {
        double cost=0;
        if(itemReader.getShapeOrType()[pointer2].equals("Tile")) {
            double piece = Math.ceil(wallArea / itemReader.getLengthOrArea()[pointer2]);
            cost += (piece * itemReader.getWidthOrPrice()[pointer2]);
        }
        else{
            cost += Math.ceil(wallArea * itemReader.getWidthOrPrice()[pointer2]);
        }

        double piece = Math.ceil(floorArea / itemReader.getLengthOrArea()[pointer3]);
        cost += (piece * itemReader.getWidthOrPrice()[pointer3]);
        return (int)cost;
    }

    @Override
    public double SetWallsArea(int pointer) {
        if (itemReader.getShapeOrType()[pointer].equals("Circle")) {
            double r = itemReader.getLengthOrArea()[pointer] /2.0;
            double h = itemReader.getHeight()[pointer];

            return 2 * Math.PI *r * h;
        }
        else if (itemReader.getShapeOrType()[pointer].equals("Rectangle")) {
            int a = itemReader.getLengthOrArea()[pointer];
            int b = itemReader.getWidthOrPrice()[pointer];
            int h = itemReader.getHeight()[pointer];

            return 2 * (a + b) * h;
        }
        return 0;
    }

    @Override
    public double SetFloorsArea(int pointer) {
        if (itemReader.getShapeOrType()[pointer].equals("Circle")) {
            double r = itemReader.getLengthOrArea()[pointer]/2.0;
            return Math.PI *r * r;
        }
        else if (itemReader.getShapeOrType()[pointer].equals("Rectangle")) {
            int a = itemReader.getLengthOrArea()[pointer];
            int b = itemReader.getWidthOrPrice()[pointer];
            return a * b;
        }
        return 0;
    }

    @Override
    public int SetPointer(String className) {
        for(int i = 0 ; i < itemReader.getName().length; i++ ){
            if(className.equals(itemReader.getName()[i])){
                return i;
            }
        }
        return -1;
    }

}
