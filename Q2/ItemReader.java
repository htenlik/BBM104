import java.util.ArrayList;
import java.util.List;

public class ItemReader {

    private String[] word;
    private String[] name;
    private String[] shapeOrType;
    private int[] widthOrPrice;
    private int[] lengthOrArea;
    private int[] height;

    public void itemInitialize(String[] items) {
        List<String> wordsList = new ArrayList<>();
        List<String> namesList = new ArrayList<>();
        List<String> shapesList = new ArrayList<>();
        List<Integer> widthsList = new ArrayList<>();
        List<Integer> lengthsList = new ArrayList<>();
        List<Integer> heightsList = new ArrayList<>();

        for (String item : items) {
            String[] parts = item.split("\t");
            wordsList.add(parts.length > 0 ? parts[0] : "");
            namesList.add(parts.length > 1 ? parts[1] : "");
            shapesList.add(parts.length > 2 ? parts[2] : "");
            widthsList.add(parts.length > 3 ? Integer.parseInt(parts[3]) : 0);
            lengthsList.add(parts.length > 4 ? Integer.parseInt(parts[4]) : 0);
            heightsList.add(parts.length > 5 ? Integer.parseInt(parts[5]) : 0);
        }

        word = wordsList.toArray(new String[0]);
        name = namesList.toArray(new String[0]);
        shapeOrType = shapesList.toArray(new String[0]);
        widthOrPrice = widthsList.stream().mapToInt(i->i).toArray(); // to changing integer list to array
        lengthOrArea = lengthsList.stream().mapToInt(i->i).toArray();
        height = heightsList.stream().mapToInt(i->i).toArray();

    } // itemInitialize


    // Getters

    public String[] getWord() {
        return word;
    }

    public String[] getName() {
        return name;
    }

    public String[] getShapeOrType() {
        return shapeOrType;
    }

    public int[] getWidthOrPrice() {
        return widthOrPrice;
    }

    public int[] getLengthOrArea() {
        return lengthOrArea;
    }

    public int[] getHeight() {
        return height;
    }

} // class
