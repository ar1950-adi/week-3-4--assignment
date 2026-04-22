import java.util.Arrays;

public class Problem6RiskThresholdBinaryLookup {
    record SearchResult(int index, int comparisons) {
    }

    record BandResult(int insertionPoint, Integer floor, Integer ceiling, int comparisons) {
    }

    public static void main(String[] args) {
        int[] unsortedRiskBands = {50, 10, 100, 25};
        int[] sortedRiskBands = {10, 25, 50, 100};
        int target = 30;

        SearchResult linear = linearSearch(unsortedRiskBands, target);
        BandResult binary = binaryBandLookup(sortedRiskBands, target);

        System.out.println("Unsorted risks: " + Arrays.toString(unsortedRiskBands));
        System.out.println("Sorted risks: " + Arrays.toString(sortedRiskBands));
        System.out.println("Linear threshold=" + target + ": "
                + (linear.index() == -1 ? "not found" : "index " + linear.index())
                + " (" + linear.comparisons() + " comparisons)");
        System.out.println("Binary insertionPoint=" + binary.insertionPoint()
                + ", floor=" + binary.floor()
                + ", ceiling=" + binary.ceiling()
                + " (" + binary.comparisons() + " comparisons)");
    }

    static SearchResult linearSearch(int[] riskBands, int target) {
        int comparisons = 0;

        for (int i = 0; i < riskBands.length; i++) {
            comparisons++;
            if (riskBands[i] == target) {
                return new SearchResult(i, comparisons);
            }
        }

        return new SearchResult(-1, comparisons);
    }

    static BandResult binaryBandLookup(int[] sortedRiskBands, int target) {
        int low = 0;
        int high = sortedRiskBands.length - 1;
        int insertionPoint = sortedRiskBands.length;
        int comparisons = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            comparisons++;

            if (sortedRiskBands[mid] >= target) {
                insertionPoint = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        Integer floor = insertionPoint > 0 ? sortedRiskBands[insertionPoint - 1] : null;
        Integer ceiling = insertionPoint < sortedRiskBands.length ? sortedRiskBands[insertionPoint] : null;

        if (ceiling != null && ceiling == target) {
            floor = target;
        }

        return new BandResult(insertionPoint, floor, ceiling, comparisons);
    }
}
