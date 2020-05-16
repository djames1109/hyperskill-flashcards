package flashcards;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final Random random = new Random();
    private static final Scanner scan = new Scanner(System.in);
    private static final List<Card> cards = new ArrayList<>();
    private static final List<String> logContainer = new ArrayList<>();

    public static void main(String[] args) {
        List<String> argList = Arrays.asList(args);
        if (argList.contains("-import")) {
            importFile(argList.get(argList.indexOf("-import") + 1));
        }
        while (true) {
            print("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            Option option = Option.getByValue(nextLine());

            switch (option) {
                case ADD:
                    add();
                    break;
                case REMOVE:
                    remove();
                    break;
                case IMPORT:
                    print("File name:");
                    String importPath = nextLine();
                    importFile(importPath);
                    break;
                case EXPORT:
                    print("File name:");
                    String exportPath = nextLine();
                    exportFile(exportPath);
                    break;
                case ASK:
                    ask();
                    break;
                case LOG:
                    log();
                    break;
                case HARDEST_CARD:
                    hardestCard();
                    break;
                case RESET_STATS:
                    resetStats();
                    break;
                case EXIT:
                default:
                    print("Bye bye!");
                    if (argList.contains("-export")) {
                        exportFile(argList.get(argList.indexOf("-export") + 1));
                    }
                    return;
            }
        }
    }

    public static void add() {
        print("The card:");
        String cardName = nextLine();
        if (getCardByName(cardName).isPresent()) {
            print(Message.CARD_EXISTS.getTemplate(), cardName);
            return;
        }

        print("The definition of the card:");
        String cardDefinition = nextLine();
        if (getCardByDefinition(cardDefinition).isPresent()) {
            print(Message.DEFINITION_ALREADY_EXISTS.getTemplate(), cardDefinition);
            return;
        }
        cards.add(new Card(cardName, cardDefinition, 0));
        print(Message.CARD_ADDED.getTemplate(), cardName, cardDefinition);
    }

    public static void remove() {
        print("The card:");
        String cardName = nextLine();
        if (getCardByName(cardName).isEmpty()) {
            print(Message.CARD_NOT_FOUND.getTemplate(), cardName);
            return;
        }
        cards.removeIf(c -> cardName.equalsIgnoreCase(c.getName()));
        print(Message.CARDS_REMOVED.getTemplate());
    }

    public static void importFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            print(Message.FILE_NOT_FOUND.getTemplate());
            return;
        }
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(path)));
            String[] lines = fileContent.split("\n");
            int count = 0;
            for (String line : lines) {
                String cardName = line.split(",")[0];
                String cardDefinition = line.split(",")[1];
                int mistakes = Integer.parseInt(line.split(",")[2]);
                Optional<Card> card = getCardByName(cardName);
                if (card.isPresent()) {
                    card.get().setDefinition(cardDefinition);
                    card.get().setMistakes(mistakes);
                } else {
                    cards.add(new Card(cardName, cardDefinition, mistakes));
                }
                count++;
            }
            print(Message.CARDS_LOADED.getTemplate(), count);
        } catch (IOException e) {
            print(Message.FILE_NOT_FOUND.getTemplate());
        }
    }

    public static void exportFile(String path) {
        File file = new File(path);
        int count = 0;
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (Card card : cards) {
                fileWriter.write(card.getName() + "," + card.getDefinition() + "," + card.getMistakes() + "\n");
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        print(Message.CARDS_SAVED.getTemplate(), count);
    }

    public static void ask() {
        print("How many times to ask?");
        int numberOfQuestions = Integer.parseInt(nextLine());
        for (int count = 0; count < numberOfQuestions; count++) {
            Card card = cards.get(random.nextInt(cards.size()));
            print("Print the definition of \"%s\":\n", card.getName());
            String answer = nextLine();
            if (card.getDefinition().equalsIgnoreCase(answer)) {
                print(Message.CORRECT_ANSWER.getTemplate());
            } else {
                Optional<Card> differentCard = getCardByDefinition(answer);
                if (differentCard.isPresent()) {
                    print(Message.DIFFERENT_ANSWER.getTemplate(), card.getDefinition(), differentCard.get().getName());
                } else {
                    print(Message.WRONG_ANSWER.getTemplate(), card.getDefinition());
                }
                card.addMistake();
            }
        }
    }

    public static void log() {
        print("File name:");
        String path = nextLine();
        File file = new File(path);

        try (FileWriter fileWriter = new FileWriter(file)) {
            for (String line : logContainer) {
                fileWriter.write(line + "\n");
            }
            print("The log has been saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void hardestCard() {
        cards.sort(Collections.reverseOrder(Comparator.comparingInt(Card::getMistakes)));
        if (cards.stream().allMatch(c -> c.getMistakes() == 0)) {
            print("There are no cards with errors.");
            return;
        }

        List<Card> hardestCards = cards.stream().filter(c -> c.getMistakes() == cards.get(0).getMistakes())
                .collect(Collectors.toList());
        if (hardestCards.size() == 1) {
            print("The hardest card is \"%s\". You have %d errors answering it.",
                    hardestCards.get(0).getName(), hardestCards.get(0).getMistakes());
        } else {
            String names = cards.stream().map(Card::getName)
                    .map(c -> String.format("\"%s\"", c))
                    .collect(Collectors.joining(", "));
            print("The hardest cards are %s. You have %d errors answering them.", names, hardestCards.get(0).getMistakes());
        }
    }

    public static void resetStats() {
        cards.forEach(Card::resetMistakes);
        print("Card statistics has been reset.");
    }

    private static Optional<Card> getCardByName(String cardName) {
        return cards.stream().filter(c -> c.getName().equalsIgnoreCase(cardName)).findFirst();
    }

    private static Optional<Card> getCardByDefinition(String cardDefinition) {
        return cards.stream().filter(c -> c.getDefinition().equalsIgnoreCase(cardDefinition)).findFirst();
    }

    private static void print(String log) {
        System.out.println(log);
        logContainer.add(log);
    }

    private static void print(String template, Object... args) {
        String log = String.format(template, args);
        System.out.println(log);
        logContainer.add(log);
    }

    private static String nextLine() {
        String input = scan.nextLine();
        logContainer.add(input);
        return input;
    }
}