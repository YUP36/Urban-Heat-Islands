public class Location{
    Double reduxTemp;
    Double percentInPoverty;
    Double percentGreenSpace;

    public Location(Double rt, Double pip, Double pgs) {
        reduxTemp = rt;
        percentInPoverty = pip;
        percentGreenSpace = pgs;
    }

    @Override
    public String toString() {
        String ret = "Temperature reduction: " + reduxTemp + "\n";
        ret += "Percent below poverty line: " + percentInPoverty + "\n";
        ret += "Percent green space: " + percentGreenSpace + "\n";
        return ret;
    }
}
