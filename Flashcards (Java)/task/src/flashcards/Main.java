package flashcards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String IMPORT = "import";
    private static final String EXPORT = "export";
    private static final String ASK = "ask";
    private static final String EXIT = "exit";
    private static final String LOG = "log";
    private static final String HARDEST_CARD = "hardest card";
    private static final String RESET_STATS = "reset stats";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String userAction = "";
        List<String> logMessages = new ArrayList<>();
        do {
            FlashCardModel.logMessage(logMessages, ""); //printing a blank line
            String logMessage = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):";
            FlashCardModel.logMessage(logMessages, logMessage);

            if(args.length> 0 && Arrays.asList(args).contains("-import")) {
                String fileName = args[0].equals("-import") ? args[1] : args[3];
                FlashCardModel.logMessage(logMessages, fileName, false);
                FlashCardModel.importCardsFromFile(fileName, logMessages);
            }

            userAction = scanner.nextLine();
            FlashCardModel.logMessage(logMessages, userAction, false);
            switch (userAction) {
                case ADD -> FlashCardModel.addCard(scanner, logMessages);
                case REMOVE -> FlashCardModel.removeCard(scanner, logMessages);
                case IMPORT -> {
                    FlashCardModel.logMessage(logMessages, "File name:");
                    String fileName = scanner.nextLine();

                    FlashCardModel.logMessage(logMessages, fileName, false);
                    FlashCardModel.importCardsFromFile(fileName, logMessages);
                }
                case EXPORT ->  {
                    FlashCardModel.logMessage(logMessages, "File name:");
                    String fileName = scanner.nextLine();

                    FlashCardModel.logMessage(logMessages, fileName, false);
                    FlashCardModel.exportCardsToFile(fileName, logMessages);
                }
                case ASK -> FlashCardModel.askQuestions(scanner, logMessages);
                case EXIT -> {
                    if(args.length> 0 && Arrays.asList(args).contains("-export")) {
                        String fileName = args[0].equals("-export") ? args[1] : args[3];
                        FlashCardModel.logMessage(logMessages, fileName, false);
                        FlashCardModel.exportCardsToFile(fileName, logMessages);
                    }
                    FlashCardModel.doExit(logMessages);
                }
                case LOG -> FlashCardModel.saveLogsToFile(scanner, logMessages);
                case HARDEST_CARD -> FlashCardModel.printHardestCard(logMessages);
                case RESET_STATS -> FlashCardModel.resetStats(logMessages);
                default -> FlashCardModel.logMessage(logMessages, "Invalid action.");
            }
        } while (!userAction.equals(UserAction.EXIT.getAction()));
    }



}
