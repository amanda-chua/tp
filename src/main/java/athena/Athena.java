package athena;

import athena.commands.Command;
import athena.exceptions.CommandException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * The main class of the ATHENA program.
 */
public class Athena {
    private Ui ui;
    private Parser parser;
    private Storage storage;
    private TaskList taskList;

    /**
     * Creates an ATHENA object.
     */
    public Athena() {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage("data.csv");
    }

    public static void main(String[] args) {
        Athena athena = new Athena();
        athena.runProgram();
    }

    /**
     * Runs the main code of the ATHENA program.
     */
    public void runProgram() {
        String inputString;
        Command userCommand;

        ui.printWelcomeMessage();

        try {
            taskList = storage.loadTaskListData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            ui.printInvalidTask();
        }
        boolean isExit = false;
        Scanner input = new Scanner(System.in);

        while (!isExit) {
            try {
                inputString = input.nextLine();
                userCommand = parser.parse(inputString, taskList);
                userCommand.execute(taskList, ui);
                storage.saveTaskListData(taskList);
                isExit = userCommand.getIsExit();
            } catch (CommandException e) {
                e.printErrorMessage();
            }
            continue;
        }
    }
}
