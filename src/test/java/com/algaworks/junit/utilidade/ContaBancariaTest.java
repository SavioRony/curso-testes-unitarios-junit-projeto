package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ContaBancariaTest {

    private ContaBancaria conta;

    @BeforeEach
    void setUp() {
        conta = new ContaBancaria(new BigDecimal("100.00"));
    }

    @Nested
    class CriarConta{

        @Test
        void deveCriarContaComSaldoValido() {
            // Arrange
            BigDecimal saldoInicial = new BigDecimal("50.00");

            // Act
            ContaBancaria novaConta = new ContaBancaria(saldoInicial);

            // Assert
            assertEquals(saldoInicial, novaConta.saldo());
        }

        @Test
        void deveLancarExcecaoAoCriarContaComSaldoNulo() {
            // Arrange
            BigDecimal saldoInicial = null;

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> new ContaBancaria(saldoInicial));
        }
    }

    @Nested
    class Deposito {
        @Test
        void deveRealizarDepositoComValorValido() {
            // Arrange
            BigDecimal valorDeposito = new BigDecimal("25.00");

            // Act
            conta.deposito(valorDeposito);

            // Assert
            assertEquals(new BigDecimal("125.00"), conta.saldo());
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"0", "-10.00", "-0.01"})
        void deveLancarExcecaoAoDepositarValorInvalido(String valorStr) {
            // Arrange
            BigDecimal valor = valorStr == null ? null : new BigDecimal(valorStr);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> conta.deposito(valor));
        }
    }


    @Nested
    class Saque{
        @Test
        void deveRealizarSaqueComValorValido() {
            // Arrange
            BigDecimal valorSaque = new BigDecimal("40.00");

            // Act
            conta.saque(valorSaque);

            // Assert
            assertEquals(new BigDecimal("60.00"), conta.saldo());
        }

        @Test
        void deveLancarExcecaoAoSacarValorMaiorQueSaldo() {
            // Arrange
            BigDecimal valorSaque = new BigDecimal("150.00");

            // Act & Assert
            assertThrows(RuntimeException.class, () -> conta.saque(valorSaque));
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"0", "-20.00", "-0.01"})
        void deveLancarExcecaoAoSacarValorInvalido(String valorStr) {
            // Arrange
            BigDecimal valor = valorStr == null ? null : new BigDecimal(valorStr);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> conta.saque(valor));
        }

        @Test
        void deveRetornarSaldoAtual() {
            // Arrange
            BigDecimal saldoEsperado = new BigDecimal("100.00");

            // Act
            BigDecimal saldoAtual = conta.saldo();

            // Assert
            assertEquals(saldoEsperado, saldoAtual);
        }
    }

}
