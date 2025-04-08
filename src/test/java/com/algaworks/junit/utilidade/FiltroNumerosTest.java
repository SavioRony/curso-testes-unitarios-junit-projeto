package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FiltroNumerosTest {

    @Test
    public void Dado_uma_lista_de_numeros_Quando_filtrar_por_pares_Entao_deve_retorna_numeros_pares(){
        List<Integer> numeros = Arrays.asList(1,2,3,4);
        List<Integer> numerosParesEsperados = Arrays.asList(2,4);
        List<Integer> resultadoDiltro = FiltroNumeros.numerosPares(numeros);
        assertIterableEquals(numerosParesEsperados, resultadoDiltro);
    }

    @Test
    public void Dado_uma_lista_de_numeros_Quando_filtrar_por_impares_Entao_deve_retorna_numeros_impares(){
        List<Integer> numeros = Arrays.asList(1,2,3,4);
        List<Integer> numerosImparesEsperados = Arrays.asList(1,3);
        List<Integer> resultadoDiltro = FiltroNumeros.numerosImpares(numeros);
        assertIterableEquals(numerosImparesEsperados, resultadoDiltro);
    }
}