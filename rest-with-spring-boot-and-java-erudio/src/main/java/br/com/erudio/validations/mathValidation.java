package br.com.erudio.validations;

import br.com.erudio.exception.UnsupportedMathOperationException;

public class mathValidation {

    public static boolean isNumber(String number) {
        if (number == null || number.isEmpty()) {
            throw new UnsupportedMathOperationException("Insira um valor válido !");
        }
        String n = number.replace(",",".");
        return n.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public static Double convertDouble(String number) {
        if (number == null || number.isEmpty()) {
            throw new UnsupportedMathOperationException("Insira um valor válido !");
        }
        return Double.parseDouble(number);
    }

    public static Integer convertInteger(String number) {
        if (number == null || number.isEmpty()) {
            throw new UnsupportedMathOperationException("Insira um valor válido !");
        }
        return Integer.parseInt(number);
    }
}
