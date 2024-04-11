package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Transaction {
    private String date;
    private double amount;
    private String category;

    public Transaction(String date, double amount, String category) {
        this.date = date;
        this.amount = amount;
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }
}

class CSVReader {
    public static List<Transaction> readTransactionsFromCSV(String filePath) throws IOException {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Пропускаємо заголовок файлу
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String date = data[0];
                double amount = Double.parseDouble(data[1]);
                String category = data[2];
                transactions.add(new Transaction(date, amount, category));
            }
        }

        return transactions;
    }
}

class TransactionAnalyzer {
    public static double calculateTotalBalance(List<Transaction> transactions) {
        double totalBalance = 0;
        for (Transaction transaction : transactions) {
            totalBalance += transaction.getAmount();
        }
        return totalBalance;
    }

    public static Map<String, Integer> countTransactionsByMonth(List<Transaction> transactions) {
        Map<String, Integer> transactionsByMonth = new HashMap<>();
        for (Transaction transaction : transactions) {
            String[] dateParts = transaction.getDate().split("-");
            String month = dateParts[1];
            transactionsByMonth.put(month, transactionsByMonth.getOrDefault(month, 0) + 1);
        }
        return transactionsByMonth;
    }
}

class ReportGenerator {
    public static void generateReport(double totalBalance, Map<String, Integer> transactionsByMonth) {
        System.out.println("Total balance: " + totalBalance);
        System.out.println("Transactions by month:");
        for (Map.Entry<String, Integer> entry : transactionsByMonth.entrySet()) {
            System.out.println("Month: " + entry.getKey() + ", Transactions: " + entry.getValue());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        String fileName = "transactions.csv";
        String filePath = System.getProperty("user.dir") + "/" + fileName;

        try {
            List<Transaction> transactions = CSVReader.readTransactionsFromCSV(filePath);

            // Розрахунок загального балансу
            double totalBalance = TransactionAnalyzer.calculateTotalBalance(transactions);

            // Підрахунок транзакцій за місяць
            Map<String, Integer> transactionsByMonth = TransactionAnalyzer.countTransactionsByMonth(transactions);

            // Генерація звіту
            ReportGenerator.generateReport(totalBalance, transactionsByMonth);

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }
}
