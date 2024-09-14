package bg.sofia.uni.fmi.mjt.ethanolchat.client;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Common {
    public static final String ENTER_CODE = "Enter code: ";
    public static final String ENTER_USERNAME = "Enter username: ";
    private static final String CLEAR_CONSOLE = "\033[H\033[2J";
    private static final String VALID_INPUT = "Valid characters are: letters, digits, -, _, .";
    private static final Pattern INPUT = Pattern.compile("^[\\w\\d\\-_.]+$");

    public static void clearConsole() {
        System.out.print(CLEAR_CONSOLE);
        System.out.flush();
    }

    public static String makeCredentials(String username, String code, boolean isChat) {
        return STR. "\{ isChat ? 1 : 0 }|\{ username }|\{ code }" ;
    }

    public static boolean isValid(String input) {
        return INPUT.matcher(input).matches();
    }

    public static void showValidInput() {
        System.out.println(VALID_INPUT);
    }

    public static String getInput(String inputMessage, Scanner scanner) {
        String input = "";

        while (!isValid(input)) {
            showValidInput();
            System.out.print(inputMessage);
            input = scanner.nextLine().strip();
        }

        return input;
    }
}
