import java.util.*;

public class ExecutionOrderChecker {

    // Returns a list of all possible orders in which the operations can be executed to satisfy the sequential consistency constraints
    public static List<List<MethodCall>> findPossibleOrders(List<MethodCall> operations) {
        List<List<MethodCall>> results = new ArrayList<>();
        acquireOrders(new ArrayList<>(), operations, results, new LinkedList<>());
        return results;
    }

    private static void acquireOrders(List<MethodCall> currentOrder, List<MethodCall> remainingOperations, List<List<MethodCall>> results, LinkedList<String> fifoQueue) {
        if (remainingOperations.isEmpty()) {
            results.add(new ArrayList<>(currentOrder));
            return;
        }

        for (int i = 0; i < remainingOperations.size(); i++) {
            MethodCall selectedCall = remainingOperations.get(i);
            
            if (allowedToSelect(currentOrder, selectedCall, fifoQueue)) {
                if (selectedCall.action.startsWith("enq")) {
                    String itemToEnqueue = selectedCall.action.substring(4, 5); // Extract the item being enqueued
                    fifoQueue.addLast(itemToEnqueue);
                } else if (selectedCall.action.startsWith("deq")) {
                    String itemToDequeue = selectedCall.action.substring(4, 5); // Extract the item being dequeued
                    fifoQueue.removeFirst(); // Ensure the correct item is dequeued
                }

                currentOrder.add(selectedCall);
                List<MethodCall> newRemaining = new ArrayList<>(remainingOperations);
                newRemaining.remove(i);
                acquireOrders(currentOrder, newRemaining, results, fifoQueue);
                currentOrder.remove(currentOrder.size() - 1);

                if (selectedCall.action.startsWith("enq")) {
                    fifoQueue.removeLast();
                } else if (selectedCall.action.startsWith("deq")) {
                    String itemToDequeue = selectedCall.action.substring(4, 5);
                    fifoQueue.addFirst(itemToDequeue);
                }
            }
        }
    }

    private static boolean allowedToSelect(List<MethodCall> currentOrder, MethodCall selectedCall, LinkedList<String> fifoQueue) {
        for (MethodCall call : currentOrder) {
            if (call.threadId.equals(selectedCall.threadId) && call.orderInThread >= selectedCall.orderInThread) {
                return false;
            }
        }

        if (selectedCall.action.startsWith("deq")) {
            String itemToDequeue = selectedCall.action.substring(4, 5);
            return !fifoQueue.isEmpty() && fifoQueue.getFirst().equals(itemToDequeue);
        }

        return true;
    }
}
