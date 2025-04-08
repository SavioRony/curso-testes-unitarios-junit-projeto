package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes no utilitário de saudação")
class SaudacaoUtilTest {

    @Test
    @DisplayName("Deve saudar com bom dia")
    public void saudarBomDia(){
        String saudacao = SaudacaoUtil.saudar(9);
        assertEquals("Bom dia", saudacao,"Saudação incorreta");

    }

    @Test
    @DisplayName("Deve saudar com bom dia apartir das 5h")
    public void saudarBomDiaApartir5h(){
        String saudacao = SaudacaoUtil.saudar(5);
        assertEquals("Bom dia", saudacao,"Saudação incorreta");

    }


    @Test
    @DisplayName("Deve saudar com boa tarde")
    public void saudarBoatarde(){
        String saudacao = SaudacaoUtil.saudar(12);
        assertEquals("Boa tarde", saudacao,"Saudação incorreta");

    }

    @Test
    @DisplayName("Deve saudar com boa noite")
    public void saudarBoaNoite(){
        String saudacao = SaudacaoUtil.saudar(18);
        assertEquals("Boa noite", saudacao,"Saudação incorreta");

    }

    @Test
    @DisplayName("Deve saudar com boa noite as 4h")
    public void saudarBoaNoiteAs4h(){
        String saudacao = SaudacaoUtil.saudar(4);
        assertEquals("Boa noite", saudacao,"Saudação incorreta");

    }

    @Test
    @DisplayName("Deve lancar exception com hora invalida")
    public void deveLancarException(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> SaudacaoUtil.saudar(-10), "Deve lançar exception");
        assertEquals("Hora invalida", exception.getMessage());
    }

}