public interface Builder {

    int SetPointer(String className);
    double SetWallsArea(int pointer);
    double SetFloorsArea(int pointer);
    String SetWalls(int pointer, double wallArea);
    String SetFloors(int pointer, double floorArea);
    int SetPrices(int pointer1, int pointer2, double wallArea, double floorArea);


}
