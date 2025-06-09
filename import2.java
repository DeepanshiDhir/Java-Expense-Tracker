import java.io.*;
import java.util.*;

class Expense implements Serializable {
    private String category;
    private double amount;
    private String date;

    public Expense(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date + " - " + category + ": $" + amount;
    }
}

public class ExpenseTracker {
    private static final String DATA_FILE = "expenses.dat";
    private List<Expense> expenses;

    public ExpenseTracker() {
        expenses = loadExpenses();
    }

    public void addExpense(String category, double amount, String date) {
        expenses.add(new Expense(category, amount, date));
        saveExpenses();
    }

    public void showExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        for (Expense e : expenses) {
            System.out.println(e);
        }
    }

    public void showCategorySummary() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        Map<String, Double> summary = new HashMap<>();
        for (Expense e : expenses) {
            summary.put(e.getCategory(), summary.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
        }
        System.out.println("Expense Summary by Category:");
        for (String category : summary.keySet()) {
            System.out.printf("%s: $%.2f\n", category, summary.get(category));
        }
    }

    private void saveExpenses() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(expenses);
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Expense> loadExpenses() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (List<Expense>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nExpense Tracker Menu:");
            System.out.println("1. Add Expense");
            System.out.println("2. Show Expenses");
            System.out.println("3. Show Summary by Category");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine(); // consume newline
                    System.out.print("Enter date (YYYY-MM-DD): ");
                    String date = scanner.nextLine();
                    tracker.addExpense(category, amount, date);
                }
                case 2 -> tracker.showExpenses();
                case 3 -> tracker.showCategorySummary();
                case 4 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }
}
