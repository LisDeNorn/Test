import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BullsCowsTest extends StageTest<String> {

    @Override
    public List<TestCase<String>> generate() {
        return Arrays.asList(
                new TestCase<String>().setDynamicTesting(this::test1),
                new TestCase<String>().setDynamicTesting(this::test2),
                new TestCase<String>().setDynamicTesting(this::test3));
    }

    String secretNumber;

    // base test
    CheckResult test1() {
        TestedProgram main = new TestedProgram();
        main.start();
        String input = "3601";
        String output = main.execute(input);
        return solver(input, output);
    }

    // test with 4 bulls
    CheckResult test2() {
        TestedProgram main = new TestedProgram();
        main.start();
        String output = main.execute(secretNumber);

        return solver(secretNumber, output);
    }

    // test of None result
    CheckResult test3() {
        TestedProgram main = new TestedProgram();
        main.start();
        List<Integer> source = stringToArrayOfNumbers("1234567890");
        List<Integer> secretNumberList = stringToArrayOfNumbers(secretNumber);
        source.removeAll(secretNumberList);
        String input = source.stream().map(String::valueOf).collect(Collectors.joining()).substring(0, 4);
        String output = main.execute(input);
        return solver(input, output);
    }

    CheckResult solver(String input, String output) {

        if (!findPairsOfBullsAndCows(output)) {
            return CheckResult.wrong(
                    "The testing system didn't find a pairs of " +
                            "bulls and cows or None in your program's output.");
        }

        Matcher matcher = getFourDigitsMatcher(output);
        if (!findFourDigitsWithRegExp(matcher)) {
            return CheckResult.wrong(
                    "The testing system didn't find a \"secret\" " +
                            "number in your program's output.");
        }

        secretNumber = getFourDigits(matcher);
        if (secretNumber.equals("9305")) {
            System.out.println("Why 9305? Make your own secret number :)");
        }

        int[] correctAnswer = grader(input, secretNumber);
        int[] receivedAnswers = getNumOfBullsAndCows(output);

        if (correctAnswer[0] != receivedAnswers[0]) {
            return CheckResult.wrong("The number of Bulls is incorrect.");
        }

        if (correctAnswer[1] != receivedAnswers[1]) {
            return CheckResult.wrong("The number of Cows is incorrect.");
        }

        return CheckResult.correct();
    }

    Matcher getFourDigitsMatcher(String userString) {
        Pattern fourDigitsPattern = Pattern.compile("\\b\\d{4}\\b");
        return fourDigitsPattern.matcher(userString);
    }

    boolean findFourDigitsWithRegExp(Matcher matcher) {
        return matcher.find();
    }

    String getFourDigits(Matcher matcher) {
        return matcher.group();
    }

    boolean findPairsOfBullsAndCows(String userString) {
        Pattern pairPattern = Pattern.compile("(\\b\\d ([cC]ow|[bB]ull))|[nN]one\\b");
        Matcher pairMatcher = pairPattern.matcher(userString);
        return pairMatcher.find();
    }


    // get number of bulls and cows from user program's output
    int[] getNumOfBullsAndCows(String userString) {
        Matcher nonePattern = Pattern.compile("\\b[nN]one\\b").matcher(userString);
        Matcher cowsPattern = Pattern.compile("\\b\\d [cC]ow").matcher(userString);
        Matcher bullsPattern = Pattern.compile("\\b\\d [bB]ull").matcher(userString);
        Pattern oneNumPattern = Pattern.compile("\\d");

        if (nonePattern.find()) {
            return new int[]{0, 0};
        }

        int[] ans = {0, 0};

        if (bullsPattern.find()) {
            String temp = bullsPattern.group();
            Matcher oneNumBulls = oneNumPattern.matcher(temp);
            oneNumBulls.find();
            ans[0] = Integer.parseInt(oneNumBulls.group());
        }
        if (cowsPattern.find()) {
            String temp = cowsPattern.group();
            Matcher oneNumCows = oneNumPattern.matcher(temp);
            oneNumCows.find();
            ans[1] = Integer.parseInt(oneNumCows.group());
        }

        return ans;
    }

    // reference grader
    int[] grader(String grade, String guess) {
        int bulls = 0;
        List<Integer> gradeNumbers = stringToArrayOfNumbers(grade);
        List<Integer> guessNumbers = stringToArrayOfNumbers(guess);

        for (int i = 0; i < gradeNumbers.size(); i++) {
            if (gradeNumbers.get(i).equals(guessNumbers.get(i))) {
                bulls++;
            }
        }

        gradeNumbers.retainAll(guessNumbers);
        int cows = gradeNumbers.size() - bulls;

        return new int[]{bulls, cows};
    }


    private static List<Integer> stringToArrayOfNumbers(String array) {
        return Arrays.stream(array.split(""))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
