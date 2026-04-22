import java.util.Arrays;
import java.util.Comparator;

public class Problem2ClientRiskScoreRanking {
    record Client(String id, int riskScore, double accountBalance) {
        String riskView() {
            return id + ":" + riskScore;
        }

        String fullView() {
            return id + ":" + riskScore + "($" + accountBalance + ")";
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
        Client[] clients = {
                new Client("clientC", 80, 15_000),
                new Client("clientA", 20, 45_000),
                new Client("clientB", 50, 30_000),
                new Client("clientD", 80, 25_000)
        };

        Client[] bubbleBatch = Arrays.copyOf(clients, clients.length);
        SortStats bubbleStats = bubbleSortByRiskAscending(bubbleBatch);

        Client[] insertionBatch = Arrays.copyOf(clients, clients.length);
        SortStats insertionStats = insertionSortByRiskDescThenBalance(insertionBatch);

        System.out.println("Bubble (asc): " + formatRisk(bubbleBatch));
        System.out.println("Bubble passes=" + bubbleStats.passes() + ", swaps=" + bubbleStats.swaps());
        System.out.println("Insertion (risk desc + balance): " + formatFull(insertionBatch));
        System.out.println("Insertion passes=" + insertionStats.passes() + ", shifts=" + insertionStats.shifts());
        System.out.println("Top 3 risks: " + formatRisk(topRisks(insertionBatch, 3)));
    }

    static SortStats bubbleSortByRiskAscending(Client[] clients) {
        int passes = 0;
        int swaps = 0;

        for (int i = 0; i < clients.length - 1; i++) {
            boolean swapped = false;
            passes++;

            for (int j = 0; j < clients.length - i - 1; j++) {
                if (clients[j].riskScore() > clients[j + 1].riskScore()) {
                    Client temp = clients[j];
                    clients[j] = clients[j + 1];
                    clients[j + 1] = temp;
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

    static SortStats insertionSortByRiskDescThenBalance(Client[] clients) {
        Comparator<Client> byRiskDescThenBalance = Comparator
                .comparingInt(Client::riskScore)
                .reversed()
                .thenComparingDouble(Client::accountBalance);
        int passes = 0;
        int shifts = 0;

        for (int i = 1; i < clients.length; i++) {
            Client current = clients[i];
            int j = i - 1;
            passes++;

            while (j >= 0 && byRiskDescThenBalance.compare(clients[j], current) > 0) {
                clients[j + 1] = clients[j];
                j--;
                shifts++;
            }

            clients[j + 1] = current;
        }

        return SortStats.insertion(passes, shifts);
    }

    static Client[] topRisks(Client[] sortedByRiskDesc, int count) {
        return Arrays.copyOf(sortedByRiskDesc, Math.min(count, sortedByRiskDesc.length));
    }

    private static String formatRisk(Client[] clients) {
        return Arrays.stream(clients)
                .map(Client::riskView)
                .toList()
                .toString();
    }

    private static String formatFull(Client[] clients) {
        return Arrays.stream(clients)
                .map(Client::fullView)
                .toList()
                .toString();
    }
}
