package cl.figio.automatizacion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    void debeSumarDosNumerosCorrectamente() {
        Calculator calculator = new Calculator();

        int resultado = calculator.sumar(5, 3);

        assertEquals(8, resultado);
    }

    @Test
    void debeRestarDosNumerosCorrectamente() {
        Calculator calculator = new Calculator();

        int resultado = calculator.restar(10, 4);

        assertEquals(6, resultado);
    }
}