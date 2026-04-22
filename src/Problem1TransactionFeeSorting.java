import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Problem1TransactionFeeSorting {
    private static final double HIGH_FEE_LIMIT = 50.0;

    record Transaction(String id, double fee, LocalTime timestamp) {
        String feeView() {
            return id + ":" + fee;
        }

        String fullView() {
            return id + ":" + fee + "@" + timestamp;
        }
    }

    record SortStats(int passes, int swaps, int shifts) {
        static SortStats bubble(int passes, int swaps) {
            return new SortStats(passes, swaps, 0);
        }

        static SortStats insertion(int passes, int shifts) {
            return new SortStats(passes, 0, shifts);
        }
    }

    public static void main(String[] args) {
        ArrayList<Transaction> transactions = new ArrayList<>(List.of(
                new Transaction("id1", 10.5, LocalTime.of(10, 0)),
                new Transaction("id2", 25.0, LocalTime.of(9, 30)),
                new Transaction("id3", 5.0, LocalTime.of(10, 15)),
                new Transaction("id4", 25.0, LocalTime.of(9, 45)),
                new Transaction("id5", 75.0, LocalTime.of(11, 10))
        ));

        ArrayList<Transaction> smallBatch = new ArrayList<>(transactions);
        SortStats bubbleStats = bubbleSortByFee(smallBatch);

        ArrayList<Transaction> mediumBatch = new ArrayList<>(transactions);
        SortStats insertionStats = insertionSortByFeeThenTimestamp(mediumBatch);

        System.out.println("BubbleSort by fee: " + formatFees(smallBatch));
        System.out.println("Bubble passes=" + bubbleStats.passes() + ", swaps=" + bubbleStats.swaps());
        System.out.println("InsertionSort by fee + timestamp: " + formatFull(mediumBatch));
        System.out.println("Insertion passes=" + insertionStats.passes() + ", shifts=" + insertionStats.shifts());
        System.out.println("High-fee outliers: " + formatFull(findHighFeeOutliers(transactions)));
    }

    static SortStats bubbleSortByFee(ArrayList<Transaction> transactions) {
        int swaps = 0;
        int passes = 0;

        for (int i = 0; i < transactions.size() - 1; i++) {
            boolean swapped = false;
            passes++;

            for (int j = 0; j < transactions.size() - i - 1; j++) {
                if (transactions.get(j).fee() > transactions.get(j + 1).fee()) {
                    Transaction temp = transactions.get(j);
                    transactions.set(j, transactions.get(j + 1));
                    transactions.set(j + 1, temp);
                    swaps++;
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }

        return SortStats.bubble(passes, swaps);
    }

    static SortStats insertionSortByFeeThenTimestamp(ArrayList<Transaction> transactions) {
        Comparator<Transaction> byFeeThenTimestamp = Comparator
                .comparingDouble(Transaction::fee)
                .thenComparing(Transaction::timestamp);
        int shifts = 0;
        int passes = 0;

        for (int i = 1; i < transactions.size(); i++) {
            Transaction current = transactions.get(i);
            int j = i - 1;
            passes++;

            while (j >= 0 && byFeeThenTimestamp.compare(transactions.get(j), current) > 0) {
                transactions.set(j + 1, transactions.get(j));
                j--;
                shifts++;
            }

            transactions.set(j + 1, current);
        }

        return SortStats.insertion(passes, shifts);
    }

    static List<Transaction> findHighFeeOutliers(List<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> transaction.fee() > HIGH_FEE_LIMIT)
                .toList();
    }

    private static String formatFees(List<Transaction> transactions) {
        return transactions.stream()
                .map(Transaction::feeView)
                .toList()
                .toString();
    }

    private static String formatFull(List<Transaction> transactions) {
        return transactions.stream()
                .map(Transaction::fullView)
                .toList()
                .toString();
    }
}
