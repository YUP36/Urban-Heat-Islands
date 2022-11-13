import java.util.*;
import java.io.*;

public class UrbanHeatIsland{

    static ArrayList<Location> locations;
    static final String REDUX = "AvgReduxinNighttimeAnnualTemp_Celsius";
    static final String POVERTY = "PercentPopIncomeBelow2xPovertyLevel";
    static final String GREEN = "Percent_GreenSpace";

    public void readCSV(String name) throws Exception {

        Scanner sc = new Scanner(new File(name));
        sc.nextLine();
        locations = new ArrayList<>();
        String[] info;
        while(sc.hasNext()) {
            info = sc.nextLine().strip().split(",");
            locations.add(new Location(Double.parseDouble(info[1]), 
                                        Double.parseDouble(info[2]),
                                        Double.parseDouble(info[3])));
        }
    }

    static double CONVERSION_FACTOR = 0.0113;
    static double COST_REMOVE = 1;
    static double COST_ADD = 1;

    /**
     * Calculates the predicted reduction in nighttime temperature
     * caused by a given existing greenspace.
     */
    public static double redux(double existing) {
        return CONVERSION_FACTOR * existing;
    }


    /**
     * Calculates the predicted reduction in nighttime temperature
     * caused by a given existing greenspace with an added percent
     * greenspace.
     */
    public static double redux(double existing, double added) {
        return CONVERSION_FACTOR * (existing + added);
    }

    /**
     * Returns the cost of adding a certain percentage greenspace to
     * an existing greenspace. Costs include the cost to remove existing
     * greenspace (applicable below 60% and the cost of adding greenspace.
     */
    public static double cost(double existing, double added) {
        if (existing < 60) {
            if (existing + added < 60) {
                return added * (COST_REMOVE + COST_ADD);
            }
            else {
                return (60 - existing) * COST_REMOVE + added * COST_ADD;
            }
        }
        return added * COST_ADD;
    }

    public static double costOfLocation(Location loc, Double percentCap) {
        return cost(loc.percentGreenSpace, percentCap - loc.percentGreenSpace);
    }

    // this method is used to find the percentage of green space after a certain amount of money is added
    public static double findNewPercentGreenSpace(Location loc, Double money) {
        if(loc.percentGreenSpace < 60) {
            Double guess = money / (COST_REMOVE + COST_ADD);
            if(guess > 60) {
                return guess + ((guess - 60) * (COST_REMOVE + COST_ADD) / COST_ADD);
            } else {
                return guess;
            }
        }
        return money / COST_ADD;
    }

    public void allocateBudget(Double budget, Double povertyBias, Double percentCap) {
        Double budgetForPoverty = budget * povertyBias;
        Double budgetRest = budget * (1 - povertyBias);

        Comparator<Location> comparePoverty = (a, b) -> Double.compare(b.percentInPoverty, a.percentInPoverty);
        Comparator<Location> compareCost = (a, b) -> Double.compare(costOfLocation(a, percentCap), costOfLocation(b, percentCap));
        Comparator<Location> compareGreenSpace = (a, b) -> Double.compare(b.percentGreenSpace, a.percentGreenSpace);

        Comparator<Location> c = comparePoverty.thenComparing(compareCost).thenComparing(compareGreenSpace);

        PriorityQueue<Location> pqForPoverty = new PriorityQueue<>(c);
        for(Location l : locations) {
            if(l.percentGreenSpace < percentCap) {
                pqForPoverty.add(l);
            }
        }
        System.out.println("Prioritized Lowest Income: ");
        Location next;
        double cost;
        while(budgetForPoverty > 0) {
            next = pqForPoverty.peek();
            cost = costOfLocation(next, percentCap);
            System.out.println("Cost: " + cost);
            if(budgetForPoverty < cost) {
                System.out.println(next);
                next.percentGreenSpace = findNewPercentGreenSpace(next, budgetForPoverty);
            } else {
                System.out.println(next);
                next.percentGreenSpace = percentCap;
            }
            pqForPoverty.remove();
            budgetForPoverty -= cost;
        }

        // This is the same code as before, just used to allocate the rest of the budget
        // without a bias towards helping lower income communities and instead optimizing price
        c = compareCost.thenComparing(compareGreenSpace);

        PriorityQueue<Location> pqRest = new PriorityQueue<>(c);
        for(Location l : locations) {
            if(l.percentGreenSpace < percentCap) {
                pqRest.add(l);
            }
        }
        System.out.println("Prioritized lowest price: ");
        while(budgetRest > 0) {
            next = pqRest.peek();
            cost = costOfLocation(next, percentCap);
            System.out.println("Cost: " + cost);
            if(budgetRest < cost) {
                System.out.println(next);
                next.percentGreenSpace = findNewPercentGreenSpace(next, budgetRest);
            } else {
                System.out.println(next);
                next.percentGreenSpace = percentCap;
            }
            pqRest.remove();
            budgetRest -= cost;
        }
    }
}
