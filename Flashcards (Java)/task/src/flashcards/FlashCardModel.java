package flashcards;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class FlashCardModel {
    protected static void addCard(Scanner scanner, List<String> logMessages) {
        logMessage(logMessages, "The card:");

        String term;
        term = scanner.nextLine();
        logMessage(logMessages, term, false);

        if (FlashCardValidator.isExistingTerm(term)) {
            logMessage(logMessages, "The card \"" + term + "\" already exists.");
            return;
        }
        logMessage(logMessages, "The definition of the card:");

        String definition = scanner.nextLine();
        logMessage(logMessages, definition, false);

        if (FlashCardValidator.isExistingDefinition(definition)) {
            logMessage(logMessages, "The definition \"" + definition + "\" already exists.");
        } else {
            KnowledgeRepository.getInstance().addCard(term, definition, 0);
            logMessage(logMessages, "The pair (\"" + term + "\":\"" + definition + "\") has been added.");
        }
    }

    protected static void removeCard(Scanner scanner, List<String> logMessages) {
        logMessage(logMessages, "Which card?");

        String term;
        term = scanner.nextLine();
        logMessage(logMessages, term, false);

        if (FlashCardValidator.isExistingTerm(term)) {
            KnowledgeRepository.getInstance().removeCard(term);
            logMessage(logMessages, "The card has been removed.");
        } else {
            logMessage(logMessages, "Can't remove \"" + term + "\": there is no such card.");
        }
    }

    protected static void importCardsFromFile(String fileName, List<String> logMessages) throws IOException {
        //Get the current working directory
        Path dirPath = Path.of("").toAbsolutePath();
        Path filePath = Paths.get(dirPath.toString(), fileName);

        if (Files.notExists(filePath)) {
            logMessage(logMessages, "File not found.");
            return;
        }

        int numberOfCardsImported = StorageRepository.importCards(fileName);
        logMessage(logMessages, numberOfCardsImported + " cards have been loaded.");
    }

    protected static void exportCardsToFile(String fileName, List<String> logMessages) {
        int numberOfCardsExported = StorageRepository.exportCards(fileName);
        logMessage(logMessages, numberOfCardsExported + " cards have been saved.");
    }

    protected static void askQuestions(Scanner scanner, List<String> logMessages) {
        logMessage(logMessages, "How many times to ask?");

        int numberOfQuestions = Integer.parseInt(scanner.nextLine());
        logMessage(logMessages, String.valueOf(numberOfQuestions), false);
        if (numberOfQuestions > KnowledgeRepository.getInstance().getFlashCards().size()) {
            numberOfQuestions = KnowledgeRepository.getInstance().getFlashCards().size();
        }
        for (int i = 0; i < numberOfQuestions; i++) {
            String question = KnowledgeRepository.getInstance().getFlashCards().get(i).term();
            logMessage(logMessages, "Print the definition of \"" + question + "\":");

            String answer = scanner.nextLine();
            logMessage(logMessages, answer, false);
            if (FlashCardValidator.isExistingTermAndDefinitionPair(question, answer)) {
                logMessage(logMessages, "Correct!");
            } else if (FlashCardValidator.isExistingDefinition(answer)) {
                //Increment the number of mistakes for the card
                KnowledgeRepository.getInstance().getFlashCards().get(i).incrementMistakes();
                String logMessage = "Wrong. The right answer is \""
                        + KnowledgeRepository.getInstance().getDefinitionForTerm(question) + "\", but your definition is correct for \""
                        + KnowledgeRepository.getInstance().getTermForDefinition(answer) + "\".";
                logMessage(logMessages, logMessage);
            } else {
                //Increment the number of mistakes for the card
                KnowledgeRepository.getInstance().getFlashCards().get(i).incrementMistakes();
                String logMessage = "Wrong. The right answer is \""
                        + KnowledgeRepository.getInstance().getDefinitionForTerm(question) + "\".";
                logMessage(logMessages, logMessage);
            }
        }
    }

    public static void doExit(List<String> logMessages) {
        logMessage(logMessages, "Bye bye!");
    }

    public static void saveLogsToFile(Scanner scanner, List<String> logMessages) {
        logMessage(logMessages, "File name:");

        String fileName = scanner.nextLine();
        logMessage(logMessages, fileName, false);
        logMessage(logMessages, "The log has been saved.");
        StorageRepository.logMessages(fileName, logMessages);
//        System.out.println("The log has been saved.");
    }

    public static void printHardestCard(List<String> logMessages) {
        //Get the card(s) with the highest number of mistakes
        List<KnowledgeRepository.FlashCard> hardestCards = KnowledgeRepository.getInstance().getHardestCards();
        String logMessage;
        if (hardestCards.size() == 0) {
            logMessage(logMessages, "There are no cards with errors.");
        } else if (hardestCards.size() == 1) {
            logMessage = "The hardest card is \"" + hardestCards.get(0).term() + "\". You have " + hardestCards.get(0).mistakes() + " errors answering it.";
            logMessage(logMessages, logMessage);
        } else {
            logMessage = "The hardest cards are ";
            for (KnowledgeRepository.FlashCard card : hardestCards) {
                logMessage += "\"" + card.term() + "\", ";
            }
            logMessage = logMessage.substring(0, logMessage.length() - 2);
            logMessage += ". You have " + hardestCards.get(0).mistakes() + " errors answering them.";
            logMessage(logMessages, logMessage);
        }
    }

    public static void resetStats(List<String> logMessages) {
        //Set the number of mistakes to 0 for all cards
        for (KnowledgeRepository.FlashCard card : KnowledgeRepository.getInstance().getFlashCards()) {
            card.resetMistakes();
        }
        logMessage(logMessages, "Card statistics have been reset.");
    }

    static void logMessage(List<String> logMessages, String logMessage, boolean printToConsole) {
        logMessages.add(logMessage);
        if (printToConsole)
            System.out.println(logMessage);
    }

    static void logMessage(List<String> logMessages, String logMessage) {
        logMessage(logMessages, logMessage, true);
    }
}
