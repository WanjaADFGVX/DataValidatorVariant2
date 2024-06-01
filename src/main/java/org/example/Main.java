import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Path inputFilePath = Paths.get("src", "input.txt");
        Path outputFilePath = Paths.get("src", "output.txt");

        try {
            List<String> lines = Files.readAllLines(inputFilePath, StandardCharsets.UTF_8);
            List<String> correctedLines = lines.stream()
                    .map(Main::validateAndCorrectLine)
                    .collect(Collectors.toList());

            Files.write(outputFilePath, correctedLines, StandardCharsets.UTF_8);
            System.out.println("Данные были проверены и исправлены. Проверьте результаты в output.txt");

        } catch (IOException e) {
            System.out.println("При чтении или записи файла произошла ошибка.");
            e.printStackTrace();
        }
    }

    private static String validateAndCorrectLine(String line) {
        String[] parts = line.split("\\|");

        String name = parts.length > 0 ? validateAndCorrectName(parts[0]) : "";
        String age = parts.length > 1 ? validateAndCorrectAge(parts[1]) : "";
        String phone = parts.length > 2 ? validateAndCorrectPhone(parts[2]) : "";
        String email = parts.length > 3 ? validateAndCorrectEmail(parts[3]) : "";

        return String.join("|", name, age, phone, email);
    }

    private static String validateAndCorrectName(String name) {
        Pattern pattern = Pattern.compile("^[А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+$");
        Matcher matcher = pattern.matcher(name);
        if (matcher.matches()) {
            return name;
        }

        name = name.replaceAll("([А-ЯЁ][а-яё]+)([А-ЯЁ])", "$1 $2");
        name = name.replaceAll("([А-ЯЁ][а-яё]+)([А-ЯЁ][а-яё]+)", "$1 $2");
        matcher = pattern.matcher(name);
        if (matcher.matches()) {
            return name;
        }
        return "";
    }

    private static String validateAndCorrectAge(String age) {

        String cleanedAge = age.replaceAll("[^0-9]", "");
        if (!cleanedAge.isEmpty()) {
            int ageValue = Integer.parseInt(cleanedAge);
            if (ageValue > 0 && ageValue <= 120) {
                return cleanedAge;
            }
        }
        return "";
    }

    private static String validateAndCorrectPhone(String phone) {

        String cleanedPhone = phone.replaceAll("[^0-9]", "");
        if (cleanedPhone.length() == 11 && (cleanedPhone.startsWith("7") || cleanedPhone.startsWith("8"))) {
            cleanedPhone = cleanedPhone.replaceFirst("^8", "7");
            return "+7 (" + cleanedPhone.substring(1, 4) + ") " + cleanedPhone.substring(4, 7) + "-" + cleanedPhone.substring(7, 9) + "-" + cleanedPhone.substring(9);
        }
        return "";
    }

    private static String validateAndCorrectEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return email;
        }

        email = email.replaceAll("@+", "@");
        email = email.replaceAll("\\.{2,}", ".");
        matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return email;
        }
        return "";
    }
}
