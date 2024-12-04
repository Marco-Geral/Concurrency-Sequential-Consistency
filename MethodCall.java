import java.util.Objects;

public class MethodCall {
    String threadId;
    int orderInThread;
    String action;

    public MethodCall(String threadId, int orderInThread, String action) {
        this.threadId = threadId;
        this.orderInThread = orderInThread;
        this.action = action;
    }

    @Override
    public String toString() {
        return "(" + threadId + "," + orderInThread + "," + action + ")";
    }

     @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MethodCall that = (MethodCall) obj;
        return orderInThread == that.orderInThread &&
               Objects.equals(threadId, that.threadId) &&
               Objects.equals(action, that.action);
    }

    
}