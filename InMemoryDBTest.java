public class InMemoryDBTest {
    public static void main(String[] args) {
        InMemoryDB db = new InMemoryDB();
        
        // Test Case 1: Get a non-existent key (should return null)
        System.out.println("Expected: null, Actual: " + db.get("A"));

        // Test Case 2: Try to put without a transaction (should throw an exception)
        try {
            db.put("A", 5);
        } catch (IllegalStateException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        // Test Case 3: Start a transaction and add a key (should not be visible to outside operations)
        db.beginTransaction();
        db.put("A", 5);

        // Verify that the key is not visible from outside the transaction
        System.out.println("Expected: null, Actual: " + db.get("A"));

        // Test Case 4: Commit the transaction, then check if it's visible
        //db.commit();
        //System.out.println("Expected: 5, Actual: " + db.get("A")); // Should be visible after commit

        // Test Case 5: Start a new transaction, modify the key, and check visibility within the transaction
        //db.beginTransaction();
        db.put("A", 6);
        db.commit();
        System.out.println("Expected: 6, Actual: " + db.get("A")); // Should be visible within the transaction
        try {
            db.commit();
        } catch (IllegalStateException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }
        // Test Case 6: Rollback and check if the key reverts to its previous value
        
        try {
            db.rollback();
        } catch (IllegalStateException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        System.out.println("Expected: null, Actual: " + db.get("B")); // Should revert to pre-transaction state
        db.beginTransaction();
        db.put("B", 10);
        db.rollback();
        
        
        System.out.println("Expected: null, Actual: " + db.get("B")); // Should be visible after commit
    }
}
