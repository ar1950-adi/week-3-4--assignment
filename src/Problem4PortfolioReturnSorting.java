import java.util.Arrays;
import java.util.Comparator;

public class Problem4PortfolioReturnSorting {
    record Asset(String symbol, double returnRate, double volatility) {
        String returnView() {
            return symbol + ":" + returnRate + "%";
        }

        String fullView() {
            return symbol + ":" + returnRate + "%(vol=" + volatility + ")";
        }
    }

    public static void main(String[] args) {
        Asset[] assets = {
                new Asset("AAPL", 12.0, 18.5),
                new Asset("TSLA", 8.0, 35.0),
                new Asset("GOOG", 15.0, 20.0),
                new Asset("MSFT", 12.0, 16.0)
        };

        Asset[] mergeBatch = Arrays.copyOf(assets, assets.length);
        mergeSortByReturnRate(mergeBatch);

        Asset[] quickBatch = Arrays.copyOf(assets, assets.length);
        quickSortByReturnDescThenVolatilityAsc(quickBatch, 0, quickBatch.length - 1);

        System.out.println("Merge: " + formatReturns(mergeBatch));
        System.out.println("Quick (desc + volatility): " + formatFull(quickBatch));
    }

    static void mergeSortByReturnRate(Asset[] assets) {
        Asset[] temp = new Asset[assets.length];
        mergeSort(assets, temp, 0, assets.length - 1);
    }

    private static void mergeSort(Asset[] assets, Asset[] temp, int left, int right) {
        if (left >= right) {
            return;
        }

        int mid = left + (right - left) / 2;
        mergeSort(assets, temp, left, mid);
        mergeSort(assets, temp, mid + 1, right);
        merge(assets, temp, left, mid, right);
    }

    private static void merge(Asset[] assets, Asset[] temp, int left, int mid, int right) {
        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (assets[i].returnRate() <= assets[j].returnRate()) {
                temp[k++] = assets[i++];
            } else {
                temp[k++] = assets[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = assets[i++];
        }

        while (j <= right) {
            temp[k++] = assets[j++];
        }

        for (int index = left; index <= right; index++) {
            assets[index] = temp[index];
        }
    }

    static void quickSortByReturnDescThenVolatilityAsc(Asset[] assets, int low, int high) {
        if (low >= high) {
            return;
        }

        int pivotIndex = partition(assets, low, high);
        quickSortByReturnDescThenVolatilityAsc(assets, low, pivotIndex - 1);
        quickSortByReturnDescThenVolatilityAsc(assets, pivotIndex + 1, high);
    }

    private static int partition(Asset[] assets, int low, int high) {
        int pivotIndex = medianOfThreePivot(assets, low, high);
        swap(assets, pivotIndex, high);
        Asset pivot = assets[high];
        int i = low;

        for (int j = low; j < high; j++) {
            if (compareForQuickSort(assets[j], pivot) <= 0) {
                swap(assets, i, j);
                i++;
            }
        }

        swap(assets, i, high);
        return i;
    }

    private static int compareForQuickSort(Asset first, Asset second) {
        int returnCompare = Double.compare(second.returnRate(), first.returnRate());
        if (returnCompare != 0) {
            return returnCompare;
        }
        return Double.compare(first.volatility(), second.volatility());
    }

    private static int medianOfThreePivot(Asset[] assets, int low, int high) {
        int mid = low + (high - low) / 2;
        int best = low;

        if (compareForQuickSort(assets[mid], assets[best]) > 0) {
            best = mid;
        }
        if (compareForQuickSort(assets[high], assets[best]) > 0) {
            best = high;
        }
        return best;
    }

    private static void swap(Asset[] assets, int first, int second) {
        Asset temp = assets[first];
        assets[first] = assets[second];
        assets[second] = temp;
    }

    private static String formatReturns(Asset[] assets) {
        return Arrays.stream(assets)
                .map(Asset::returnView)
                .toList()
                .toString();
    }

    private static String formatFull(Asset[] assets) {
        return Arrays.stream(assets)
                .map(Asset::fullView)
                .toList()
                .toString();
    }
}
