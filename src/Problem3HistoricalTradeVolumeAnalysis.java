import java.util.Arrays;
import java.util.Comparator;

public class Problem3HistoricalTradeVolumeAnalysis {
    record Trade(String id, int volume) {
        String view() {
            return id + ":" + volume;
        }
    }

    public static void main(String[] args) {
        Trade[] trades = {
                new Trade("trade3", 500),
                new Trade("trade1", 100),
                new Trade("trade2", 300),
                new Trade("trade4", 300)
        };

        Trade[] mergeBatch = Arrays.copyOf(trades, trades.length);
        mergeSortByVolumeAscending(mergeBatch);

        Trade[] quickBatch = Arrays.copyOf(trades, trades.length);
        quickSortByVolumeDescending(quickBatch, 0, quickBatch.length - 1);

        Trade[] morning = {new Trade("morning1", 100), new Trade("morning2", 300)};
        Trade[] afternoon = {new Trade("afternoon1", 200), new Trade("afternoon2", 300)};
        Trade[] mergedSessions = mergeSortedByVolume(morning, afternoon);

        System.out.println("MergeSort: " + format(mergeBatch));
        System.out.println("QuickSort (desc): " + format(quickBatch));
        System.out.println("Merged morning+afternoon: " + format(mergedSessions));
        System.out.println("Merged total volume: " + totalVolume(mergedSessions));
    }

    static void mergeSortByVolumeAscending(Trade[] trades) {
        Trade[] temp = new Trade[trades.length];
        mergeSort(trades, temp, 0, trades.length - 1);
    }

    private static void mergeSort(Trade[] trades, Trade[] temp, int left, int right) {
        if (left >= right) {
            return;
        }

        int mid = left + (right - left) / 2;
        mergeSort(trades, temp, left, mid);
        mergeSort(trades, temp, mid + 1, right);
        merge(trades, temp, left, mid, right);
    }

    private static void merge(Trade[] trades, Trade[] temp, int left, int mid, int right) {
        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (trades[i].volume() <= trades[j].volume()) {
                temp[k++] = trades[i++];
            } else {
                temp[k++] = trades[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = trades[i++];
        }

        while (j <= right) {
            temp[k++] = trades[j++];
        }

        for (int index = left; index <= right; index++) {
            trades[index] = temp[index];
        }
    }

    static void quickSortByVolumeDescending(Trade[] trades, int low, int high) {
        if (low >= high) {
            return;
        }

        int pivotIndex = partitionDescending(trades, low, high);
        quickSortByVolumeDescending(trades, low, pivotIndex - 1);
        quickSortByVolumeDescending(trades, pivotIndex + 1, high);
    }

    private static int partitionDescending(Trade[] trades, int low, int high) {
        int pivotIndex = medianOfThreePivot(trades, low, high);
        swap(trades, pivotIndex, high);
        Trade pivot = trades[high];
        int i = low;

        for (int j = low; j < high; j++) {
            if (trades[j].volume() > pivot.volume()) {
                swap(trades, i, j);
                i++;
            }
        }

        swap(trades, i, high);
        return i;
    }

    private static int medianOfThreePivot(Trade[] trades, int low, int high) {
        int mid = low + (high - low) / 2;
        Trade[] candidates = {trades[low], trades[mid], trades[high]};
        Arrays.sort(candidates, Comparator.comparingInt(Trade::volume));
        int medianVolume = candidates[1].volume();

        if (trades[low].volume() == medianVolume) {
            return low;
        }
        if (trades[mid].volume() == medianVolume) {
            return mid;
        }
        return high;
    }

    static Trade[] mergeSortedByVolume(Trade[] first, Trade[] second) {
        Trade[] merged = new Trade[first.length + second.length];
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < first.length && j < second.length) {
            if (first[i].volume() <= second[j].volume()) {
                merged[k++] = first[i++];
            } else {
                merged[k++] = second[j++];
            }
        }

        while (i < first.length) {
            merged[k++] = first[i++];
        }

        while (j < second.length) {
            merged[k++] = second[j++];
        }

        return merged;
    }

    static int totalVolume(Trade[] trades) {
        return Arrays.stream(trades)
                .mapToInt(Trade::volume)
                .sum();
    }

    private static void swap(Trade[] trades, int first, int second) {
        Trade temp = trades[first];
        trades[first] = trades[second];
        trades[second] = temp;
    }

    private static String format(Trade[] trades) {
        return Arrays.stream(trades)
                .map(Trade::view)
                .toList()
                .toString();
    }
}
