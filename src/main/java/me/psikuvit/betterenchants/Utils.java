package me.psikuvit.betterenchants;

public class Utils {

    public static String convertToRomanNumeral(int number) {
        if (number < 1 || number > 100) {
            throw new IllegalArgumentException("Number must be between 1 and 100 (inclusive).");
        }

        StringBuilder romanNumeral = new StringBuilder();
        int[] arabicValues = {100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanSymbols = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        for (int i = 0; i < arabicValues.length; i++) {
            while (number >= arabicValues[i]) {
                romanNumeral.append(romanSymbols[i]);
                number -= arabicValues[i];
            }
        }

        return romanNumeral.toString();
    }
}
