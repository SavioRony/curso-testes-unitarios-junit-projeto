package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class SaudacaoUtilTest {

    @Test
    public void saudarBomDia(){
        String saudacao = SaudacaoUtil.saudar(9);
        assertEquals("Bom dia", saudacao,"Saudação incorreta");

    }

    @Test
    public void saudarBomDiaApartir5h(){
        String saudacao = SaudacaoUtil.saudar(5);
        assertEquals("Bom dia", saudacao,"Saudação incorreta");

    }


    @Test
    public void saudarBoatarde(){
        String saudacao = SaudacaoUtil.saudar(12);
        assertEquals("Boa tarde", saudacao,"Saudação incorreta");

    }

    @Test
    public void saudarBoaNoite(){
        String saudacao = SaudacaoUtil.saudar(18);
        assertEquals("Boa noite", saudacao,"Saudação incorreta");

    }

    @Test
    public void saudarBoaNoiteAs4h(){
        String saudacao = SaudacaoUtil.saudar(4);
        assertEquals("Boa noite", saudacao,"Saudação incorreta");

    }

    @Test
    public void deveLancarException(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> SaudacaoUtil.saudar(-10), "Deve lançar exception");
        assertEquals("Hora invalida", exception.getMessage());
    }

    @Test
    public void naoDeveLancarException(){
        assertDoesNotThrow(() -> SaudacaoUtil.saudar(12), "Deve lançar exception");
    }
}