package mike;

import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Represents an instance of "Mike" - a simple task tracking bot.
 */
public class Mike {
    private TaskList taskList;
    private final Ui ui;
    private final Storage storage;

    //TODO: consider making Mike immutable (return Mike with new list each time)
    /**
     * Constructor for Mike.
     */
    public Mike() {
        this.ui = new Ui();
        this.taskList = new TaskList();
        this.storage = new Storage("storedList.txt"); //hardcoded for now
    }

    /**
     * Calls methods relevant to initialising "Mike".
     */
    void run() {
        ui.printGreeting();
        ui.printStartingInstruction();
        loadStoredList();

        Scanner sc = new Scanner(System.in);

        while (true) {
            requestNextInput();
            String inputString = sc.nextLine();

            if (isExitCommand(inputString)) {
                break;
            } else {
                processInput(inputString);
            }
            saveToStoredList(); //store the current list in hard drive
        }
        sc.close();
        this.close();
    }

    void close() {
        ui.printGoodbyeMessage();
    }

    void noCharactersEnteredResponse() {
        ui.printNoCharactersMessage();
    }

    void requestNextInput() {
        ui.printNextCommandInstruction();
    }

    void printList() {
        String listForPrint = taskList.getListForPrint();
        ui.printReply(listForPrint);
    }

    public boolean isExitCommand(String userInput) {
        Parser parser = new Parser(userInput);
        return parser.isBye();
    }

    void addToListWithoutReply(Task task) {
        taskList.addToList(task);
    }

    void addToListWithReply(Task task) {
        taskList.addToList(task);
        ui.printReply(taskList.addToListReply(task));
    }

    void removeTask(int removeIndex) {
        ui.printReply(taskList.removeFromListWithMessage(removeIndex));
    }

    /**
     * Adds a task of type "To-do" to the list
     *
     * @param str The name of the task to be added to the list.
     */
    void addTodo(String str) {
        Todo todo = new Todo(str);
        addToListWithReply(todo);
    }

    /**
     * Adds a task of type "Deadline" to the list.
     *
     * @param name The name of the task and its deadline in the format "taskName /by deadline".
     */
    void addDeadline(String name, String date) {
        try {
            Deadline deadline = new Deadline(name, date);
            addToListWithReply(deadline);
        } catch(DateTimeParseException e) {
            e.printStackTrace();
            System.out.println("**Enter dates in the format yyyy-mm-dd**");
        }
    }

    /**
     * Adds a task of type "Event" to the list.
     *
     * @param name The name of the task and the time of the event in the format "taskName /at eventTime".
     */
    void addEvent(String name, String scheduledDate) {
        try {
            Event event = new Event(name, scheduledDate);
            addToListWithReply(event);
        } catch(DateTimeParseException e) {
            e.printStackTrace();
            System.out.println("**Enter dates in the format yyyy-mm-dd**");
        }
    }

    /**
     * Marks the task (specified by its index in the list) as "done".
     *
     * @param indexFromUser The index of the task (as seen by the user) to be marked in the list.
     */
    void mark(int indexFromUser) {
        ui.printReply(taskList.markInListWithMessage(indexFromUser));
    }

    /**
     * Marks the task (specified by its index in the list) as "not done".
     *
     * @param indexFromUser The index of the task (as seen by the user) to be unmarked in the list.
     */
    void unmark(int indexFromUser) {
        ui.printReply(taskList.unmarkInListWithMessage(indexFromUser));
    }

    /**
     * Processes the user's input and responds accordingly.
     *
     * @param userInput String input from user passed here from Main class.
     */
    void processInput(String userInput) {
        Parser parser = new Parser(userInput);

        if (parser.isBlank()) {
            noCharactersEnteredResponse();
        }

        String command = parser.getCommand();
        try {
            switch (command) {
            case "list":
                printList();
                break;
            case "mark":
                int markIndex = parser.getIndex();
                mark(markIndex);
                break;
            case "unmark":
                int unmarkIndex = parser.getIndex();
                unmark(unmarkIndex);
                break;
            case "remove":
                int removeIndex = parser.getIndex();
                removeTask(removeIndex);
                break;
            case "todo":
                String todoName =
                        parser.getTodoName();
                addTodo(todoName);
                break;
            case "deadline":
                String deadlineName =
                        parser.getDeadlineName();
                String deadlineDate =
                        parser.getDeadlineDate();

                addDeadline(deadlineName, deadlineDate);
                break;
            case "event":
                String eventName =
                        parser.getEventName();
                String eventDate =
                        parser.getEventDate();

                addEvent(eventName, eventDate);
                break;
            default:
                String invalidCommandMessage =
                        String.format("\n**Mike: I don't understand the command \"%s\"**",
                                parser.getUserInput());
                throw new UnsupportedOperationException(invalidCommandMessage);
            }
        } catch(UnsupportedOperationException e) {
            e.printStackTrace();
        } catch(StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println("**Mike: Hmm, you may have entered the command arguments incorrectly.**\n"
                    + "**Tip: Enter dates in the format yyyy-mm-dd**");
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println("**Mike: That item number doesn't exist on your list :/**");
        }
    }

    /**
     * Stores the current list in the hard drive to be accessed on reboot.
     */
    void saveToStoredList() {
        String listInStorageFormat = taskList.convertToStoredListFormat();
        storage.storeList(listInStorageFormat);

    }

    /**
     * Loads the list from a previous session that is stored in the hard drive.
     */
    void loadStoredList() {
        this.taskList = this.taskList.convertFromStoredList(storage.loadFile());
    }
}
