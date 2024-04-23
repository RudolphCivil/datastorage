import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

// Implementation of an in-memory database with transaction support
public class InMemoryDB {
    // Main data store containing committed changes
    private final Map<String, Integer> dataStore = new HashMap<>();

    // Stack to hold transactions, allowing for rollback or commit
    private final Stack<Map<String, Integer>> transactions = new Stack<>();

    // Indicates whether a transaction is active
    private boolean inTransaction = false;

    // Returns the value for a given key, checking the current transaction first
    public Integer get(String key) {
        // Only return from base data store; ignore transaction data to avoid visibility leak
        return dataStore.get(key);
    }

    // Adds or updates a key within the current transaction
    public void put(String key, int val) {
        if (!inTransaction || transactions.isEmpty()) {
            throw new IllegalStateException("No transaction in progress.");
        }
        transactions.peek().put(key, val);  // Add or update within the current transaction
    }

    // Begins a new transaction, ensuring only one at a time
    public void beginTransaction() {
        if (inTransaction) {
            throw new IllegalStateException("Transaction already in progress.");
        }
        inTransaction = true;  // Indicate that a transaction is active
        transactions.push(new HashMap<>());  // Start a new transaction
    }

    // Commits the current transaction, applying changes to the base data store
    public void commit() {
        if (!inTransaction || transactions.isEmpty()) {
            throw new IllegalStateException("No transaction in progress.");
        }
        Map<String, Integer> currentTransaction = transactions.pop();  // Get the current transaction
        dataStore.putAll(currentTransaction);  // Apply changes to the base data store
        inTransaction = !transactions.isEmpty();  // Update transaction status
    }

    // Rolls back the current transaction, discarding its changes
    public void rollback() {
        if (!inTransaction || transactions.isEmpty()) {
            throw new IllegalStateException("No transaction in progress.");
        }
        transactions.pop();  // Discard the current transaction
        inTransaction = !transactions.isEmpty();  // Update transaction status
    }
}
