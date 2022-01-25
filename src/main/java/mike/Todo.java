package mike;

/**
 * Represents a to-do (subclass of task).
 */
public class Todo extends Task{

    private static final String taskType = "T";

    /**
     * Constructor for to-do.
     *
     * @param name Name of to-do.
     */
    public Todo(String name) {
        super(name);
    }

    /**
     * Constructor for to-do including isDone field.
     *
     * @param name Name of to-do.
     * @param isDone Whether to-do is to be marked as done.
     */
    public Todo(String name, boolean isDone) {
        super(name, isDone);
    }

    /**
     * Returns a new to-do object with the isDone field set to true.
     *
     * @return to-do object with isDone set to true.
     */
    public Todo markAsDone() {
        return new Todo(this.name, true);
    }

    /**
     * Returns a new to-do object with the isDone field set to false.
     *
     * @return to-do object with isDone set to false.
     */
    public Todo markAsUndone() {
        return new Todo(this.name, false);
    }

    /**
     * Returns a string representing the to-do that can be stored in the hard drive.
     *
     * @return String representation of to-do for storage.
     */
    public String convertToStoredTaskFormat() {
        String doneIndicator = "false";
        if (super.isDone) {
            doneIndicator = "true";
        }
        String storedListFormat = String.format("%s|%s|%s|", taskType,
                doneIndicator, super.name);
        return storedListFormat;
    }

    @Override
    public String toString() {
        String doneMark;
        if (super.isDone) {
            doneMark = "X";
        } else {
            doneMark = " ";
        }
        return String.format("[%s][%s] %s", taskType, doneMark, super.name);
    }
}
