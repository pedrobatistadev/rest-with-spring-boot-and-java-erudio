package br.com.erudio.controllers;

import br.com.erudio.exception.UnsupportedMathOperationException;
import br.com.erudio.math.OperationMath;
import br.com.erudio.validations.mathValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/math")
public class MathController {

    OperationMath math = new OperationMath();

    @RequestMapping("/sum/{number1}/{number2}")
    public Double sum(@PathVariable("number1") String number1, @PathVariable("number2") String number2) {

        if (!mathValidation.isNumber(number1) || !mathValidation.isNumber(number2)) {
            throw new UnsupportedMathOperationException("Insira um valor válido ! ");
        }

        return convertDouble(number1) + convertDouble(number2);
    }

    @RequestMapping("/subtr/{number1}/{number2}")
    public Double subtr(
            @PathVariable("number1") String number1,
            @PathVariable("number2") String number2) {

        if (!isNumber(number1) || !isNumber(number2)) {
            throw new UnsupportedMathOperationException("Insira um valor válido !");
        }

        return convertDouble(number1) - convertDouble(number2);
    }

    @RequestMapping("/multi/{number1}/{number2}")
    public Integer multi(
            @PathVariable("number1") String number1,
            @PathVariable("number2") String number2 ) {

        if (!isNumber(number1) || !isNumber(number2)) {
            throw new UnsupportedOperationException("Insira um valor válido !");
        }

        return convertInteger(number1) * convertInteger(number2);
    }

    @RequestMapping("/div/{number1}/{number2}")
    public Double div(@PathVariable("number1") String number1, @PathVariable("number2") String number2) {

        if (!mathValidation.isNumber(number1) || !mathValidation.isNumber(number2)) {
            throw new UnsupportedMathOperationException("Insira um valor válido ! ");
        }

        return convertDouble(number1) / convertDouble(number2);
    }

    @RequestMapping("/media/{number1}/{number2}")
    public Double media(
            @PathVariable("number1") String number1,
            @PathVariable("number2") String number2) {

        if (!mathValidation.isNumber(number1) || !mathValidation.isNumber(number2)) {
            throw new UnsupportedMathOperationException("Insira um valor válido ! ");
        }

        return (convertDouble(number1) + convertDouble(number2)) / 2;
    }

    @RequestMapping("/raiz/{number1}")
    public int raiz(@PathVariable("number1") String number1) {

        if (!mathValidation.isNumber(number1)) {
            throw new UnsupportedMathOperationException("Insira um valor válido ! ");
        }

        return math.raiz(mathValidation.convertInteger(number1));
    }

    public boolean isNumber(String number) {
        if (number == null || number.isEmpty()) {
            throw new UnsupportedMathOperationException("Insira um valor válido !");
        }
        String n = number.replace(",",".");
        return n.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public Double convertDouble(String number) {
        if (number == null || number.isEmpty()) {
            throw new UnsupportedMathOperationException("Insira um valor válido !");
        }
        return Double.parseDouble(number);
    }

    public Integer convertInteger(String number) {
        if (number == null || number.isEmpty()) {
            throw new UnsupportedMathOperationException("Insira um valor válido !");
        }
        return Integer.parseInt(number);
    }

}
