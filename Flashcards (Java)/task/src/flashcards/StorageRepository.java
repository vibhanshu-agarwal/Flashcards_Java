package flashcards;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageRepository {

    private static boolean DEBUG = true;
    public static int importCards(String fileName) throws IOException {
        //Get the current working directory
        Path dirPath = Path.of("").toAbsolutePath();
        Path filePath = Paths.get(dirPath.toString(), fileName);
        AtomicInteger numberOfCardsImported = new AtomicInteger();
        Files.readAllLines(filePath).forEach(line -> {
            String[] card = line.split(":");
            //If there is a card with the same term, replace it with the new one
            if (KnowledgeRepository.getInstance().getFlashCards().stream().anyMatch(flashCard -> flashCard.term().equals(card[0]))) {
                KnowledgeRepository.getInstance().removeCard(card[0]);
            }
            KnowledgeRepository.getInstance().addCard(card[0], card[1], Integer.parseInt(card[2]));
            numberOfCardsImported.getAndIncrement();
        });
        return numberOfCardsImported.get();
    }

    public static int exportCards(String fileName) {
        //Get the current working directory
        Path dirPath = Path.of("").toAbsolutePath();
        Path filePath = Paths.get(dirPath.toString(), fileName);
        AtomicInteger numberOfCardsExported = new AtomicInteger();
        //Clear all previous entries in the file
        try {
            Files.writeString(filePath, "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        KnowledgeRepository.getInstance().getFlashCards().forEach(card -> {
            try {
                Files.writeString(filePath, card.term() + ":" + card.definition() + ":" + card.mistakes() + System.lineSeparator(),
                        Files.exists(filePath) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
                numberOfCardsExported.getAndIncrement();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return numberOfCardsExported.get();
    }


    public static void logMessages(String fileName, List<String> logMessages) {
        //Get the current working directory
        Path dirPath = Path.of("").toAbsolutePath();
        Path filePath = Paths.get(dirPath.toString(), fileName);
        if(DEBUG) {
            System.out.println("dirPath: " + dirPath);
            System.out.println("filePath: " + filePath);
        }
        //Clear all previous entries
//        try {
//            Files.writeString(filePath, "");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        logMessages.forEach(logMessage -> {
            try {
                Files.writeString(filePath, logMessage + System.lineSeparator(),
                        Files.exists(filePath) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
