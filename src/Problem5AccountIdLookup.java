import java.util.Arrays;

public class Problem5AccountIdLookup {
    record SearchResult(int index, int comparisons) {
    }

    record OccurrenceResult(int firstIndex, int lastIndex, int count, int comparisons) {
    }

    public static void main(String[] args) {
        String[] logs = {"accB", "accA", "accB", "accC", "accB"};
        String target = "accB";

        SearchResult first = linearFirst(logs, target);
        SearchResult last = linearLast(logs, target);

        String[] sortedLogs = Arrays.copyOf(logs, logs.length);
        Arrays.sort(sortedLogs);
        OccurrenceResult binary = binaryOccurrences(sortedLogs, target);

        System.out.println("Logs: " + Arrays.toString(logs));
        System.out.println("Linear first " + target + ": index " + first.index()
                + " (" + first.comparisons() + " comparisons)");
        System.out.println("Linear last " + target + ": index " + last.index()
                + " (" + last.comparisons() + " comparisons)");
        System.out.println("Sorted logs: " + Arrays.toString(sortedLogs));
        System.out.println("Binary " + target + ": firstIndex " + binary.firstIndex()
                + ", lastIndex " + binary.lastIndex()
                + ", count=" + binary.count()
                + " (" + binary.comparisons() + " comparisons)");
    }

    static SearchResult linearFirst(String[] logs, String target) {
        int comparisons = 0;

        for (int i = 0; i < logs.length; i++) {
            comparisons++;
            if (logs[i].equals(target)) {
                return new SearchResult(i, comparisons);
            }
        }

        return new SearchResult(-1, comparisons);
    }

    static SearchResult linearLast(String[] logs, String target) {
        int comparisons = 0;
        int lastIndex = -1;

        for (int i = 0; i < logs.length; i++) {
            comparisons++;
            if (logs[i].equals(target)) {
                lastIndex = i;
            }
        }

        return new SearchResult(lastIndex, comparisons);
    }

    static OccurrenceResult binaryOccurrences(String[] sortedLogs, String target) {
        SearchResult first = boundarySearch(sortedLogs, target, true);
        if (first.index() == -1) {
            return new OccurrenceResult(-1, -1, 0, first.comparisons());
        }

        SearchResult last = boundarySearch(sortedLogs, target, false);
        int count = last.index() - first.index() + 1;
        return new OccurrenceResult(
                first.index(),
                last.index(),
                count,
                first.comparisons() + last.comparisons()
        );
    }

    private static SearchResult boundarySearch(String[] sortedLogs, String target, boolean findFirst) {
        int low = 0;
        int high = sortedLogs.length - 1;
        int result = -1;
        int comparisons = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            comparisons++;
            int comparison = sortedLogs[mid].compareTo(target);

            if (comparison == 0) {
                result = mid;
                if (findFirst) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return new SearchResult(result, comparisons);
    }
}
