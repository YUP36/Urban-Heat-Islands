public class Location implements Comparable<Location>{
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

    @Override
    public int compareTo(Location o) {
        Double povertyDiff = o.percentInPoverty - this.percentInPoverty;
        if(povertyDiff > 0) {
            return 1;
        } else if (povertyDiff < 0) {
            return -1;
        }
        Double greenSpaceDiff = o.percentGreenSpace - this.percentGreenSpace;
        if(greenSpaceDiff > 0) {
            return 1;
        } else if(greenSpaceDiff < 0) {
            return -1;
        }
        return 0;
    }
}
