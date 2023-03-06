package flashcards;

import java.util.*;

public class KnowledgeRepository {

    private KnowledgeRepository() {
    }

    //Make this a singleton
    private static final KnowledgeRepository INSTANCE = new KnowledgeRepository();

    public static KnowledgeRepository getInstance() {
        return INSTANCE;
    }

    private final List<FlashCard> flashCards = new ArrayList<>();

    public List<FlashCard> getFlashCards() {
        return Collections.unmodifiableList(flashCards);
    }

    public void addCard(String question, String answer, int mistakes) {
        flashCards.add(new FlashCard(question, answer, mistakes));
    }

    public void removeCard(String question) {
        flashCards.removeIf(flashCard -> flashCard.term.equals(question));
    }

    public String getTermForDefinition(String answer) {
        return Objects.requireNonNull(flashCards.stream()
                .filter(flashCard -> flashCard.definition.equals(answer))
                .findFirst()
                .orElse(null)).term();
    }

    public String getDefinitionForTerm(String term) {
        return Objects.requireNonNull(flashCards.stream()
                .filter(flashCard -> flashCard.term.equals(term))
                .findFirst()
                .orElse(null)).definition();
    }

    public List<FlashCard> getHardestCards() {
        int maxMistakes = flashCards.stream()
                .mapToInt(FlashCard::mistakes)
                .max()
                .orElse(0);

        return maxMistakes == 0 ? Collections.emptyList() : flashCards.stream()
                .filter(flashCard -> flashCard.mistakes() == maxMistakes)
                .toList();
    }

//    record FlashCard(String term, String definition, int mistakes) {
//
//    }

    static class FlashCard {

            private final String term;
            private final String definition;
            private int mistakes;

            public FlashCard(String term, String definition, int mistakes) {
                this.term = term;
                this.definition = definition;
                this.mistakes = mistakes;
            }

            public String term() {
                return term;
            }

            public String definition() {
                return definition;
            }

            public int mistakes() {
                return mistakes;
            }

            public void incrementMistakes() {
                mistakes++;
            }

            public void resetMistakes() {
                mistakes = 0;
            }
    }

}
