package flashcards;

public class FlashCardValidator {
    public static boolean isExistingTerm(String question) {
        return question != null && !question.isBlank()
                && KnowledgeRepository.getInstance().getFlashCards().stream().anyMatch(flashCard -> flashCard.term().equals(question));
    }

    public static boolean isExistingDefinition(String answer) {
        return answer != null && !answer.isBlank()
                && KnowledgeRepository.getInstance().getFlashCards().stream().anyMatch(flashCard -> flashCard.definition().equals(answer));
    }

    public static boolean isExistingTermAndDefinitionPair(String question, String answer) {
        //However, it is not the correct answer for the question
//        if(KnowledgeRepository.getInstance().getQuestionsToAnswers().containsKey(question)) {
//            return KnowledgeRepository.getInstance().getQuestionsToAnswers().get(question).equals(answer);
//        }
        KnowledgeRepository.FlashCard card = KnowledgeRepository.getInstance().getFlashCards()
                .stream()
                .filter(flashCard -> flashCard.term().equals(question) && flashCard.definition().equals(answer))
                .findFirst()
                .orElse(null);
        return card != null;
    }
}

