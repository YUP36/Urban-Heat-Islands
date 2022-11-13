public class Driver {
    public static void main(String[] args) throws Exception {
        UrbanHeatIsland Durham = new UrbanHeatIsland();
        Durham.readCSV("TriCoMM_DNC_data.csv");
        Durham.allocateBudget(300.0, 0.3, 70.0);
    }
}