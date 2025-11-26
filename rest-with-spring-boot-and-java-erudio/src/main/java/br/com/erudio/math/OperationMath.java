package br.com.erudio.math;

import br.com.erudio.validations.mathValidation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

public class OperationMath {

    public Double sum(Double number1, Double number2) {

        return number1 + number2;
    }

    public Double subtr(Double number1, Double number2) {

        return number1 - number2;
    }

    public Double multi(Double number1, Double number2) {

        return number1 * number2;
    }

    public Double div(Double number1, Double number2) {

        return number1 / number2;
    }

    public Double media(Double number1, Double number2) {

        return (number1 + number2) / 2;
    }

    public int raiz(int number1) {

        for (int i = 1; i < number1; i++) {
            if (i * i == number1) {
                return i;
            }
        }

        return 0;
    }
}
