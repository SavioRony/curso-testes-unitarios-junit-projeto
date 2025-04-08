package com.algaworks.junit.ecommerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Carrinho de compras")
public class CarrinhoCompraTest {

    Cliente cliente;
    Produto produto;
    Produto outroProduto;

    @BeforeEach
    void setup() {
        cliente = new Cliente(1L, "João");
        produto = new Produto(1L, "Teclado", "Teclado mecânico", new BigDecimal("150.00"));
        outroProduto = new Produto(2L, "Mouse", "Mouse óptico", new BigDecimal("50.00"));
    }

    @Nested
    @DisplayName("Dado um carrinho com um produto")
    class CarrinhoComProduto {

        CarrinhoCompra carrinho;

        @BeforeEach
        void init() {
            carrinho = new CarrinhoCompra(cliente);
            carrinho.adicionarProduto(produto, 2);
        }

        @Nested
        @DisplayName("Quando aumentar a quantidade do produto")
        class AumentarQuantidade {

            @BeforeEach
            void act() {
                carrinho.aumentarQuantidadeProduto(produto);
            }

            @Test
            @DisplayName("Então a quantidade deve ser atualizada")
            void deveAtualizarQuantidade() {
                assertEquals(3, carrinho.getItens().get(0).getQuantidade());
            }
        }

        @Nested
        @DisplayName("Quando diminuir a quantidade do produto")
        class DiminuirQuantidade {

            @BeforeEach
            void act() {
                carrinho.diminuirQuantidadeProduto(produto);
            }

            @Test
            @DisplayName("Então a quantidade deve ser atualizada")
            void deveAtualizarQuantidade() {
                assertEquals(1, carrinho.getItens().get(0).getQuantidade());
            }
        }

        @Nested
        @DisplayName("Quando remover o produto")
        class RemoverProduto {

            @BeforeEach
            void act() {
                carrinho.removerProduto(produto);
            }

            @Test
            @DisplayName("Então o carrinho deve ficar vazio")
            void deveRemoverProduto() {
                assertTrue(carrinho.getItens().isEmpty());
            }
        }

        @Nested
        @DisplayName("Quando obter o valor total")
        class ValorTotal {

            @Test
            @DisplayName("Então deve retornar o valor somado corretamente")
            void deveCalcularValorTotal() {
                assertEquals(new BigDecimal("300.00"), carrinho.getValorTotal());
            }
        }

        @Nested
        @DisplayName("Quando obter a quantidade total de produtos")
        class QuantidadeTotal {

            @Test
            @DisplayName("Então deve retornar a soma das quantidades")
            void deveRetornarQuantidadeTotal() {
                assertEquals(2, carrinho.getQuantidadeTotalDeProdutos());
            }
        }
    }

    @Nested
    @DisplayName("Dado um carrinho com dois produtos")
    class CarrinhoComDoisProdutos {

        CarrinhoCompra carrinho;

        @BeforeEach
        void init() {
            carrinho = new CarrinhoCompra(cliente);
            carrinho.adicionarProduto(produto, 1);
            carrinho.adicionarProduto(outroProduto, 2);
        }

        @Test
        @DisplayName("Quando esvaziar o carrinho, então não deve conter itens")
        void deveEsvaziarCarrinho() {
            carrinho.esvaziar();
            assertEquals(0, carrinho.getItens().size());
        }
    }

    @Nested
    @DisplayName("Dado um carrinho novo")
    class CarrinhoNovo {

        CarrinhoCompra carrinho;

        @BeforeEach
        void init() {
            carrinho = new CarrinhoCompra(cliente);
        }

        @Test
        @DisplayName("Quando adicionar um produto com quantidade 0, então deve lançar exceção")
        void deveLancarExcecaoParaQuantidadeZero() {
            assertThrows(IllegalArgumentException.class,
                    () -> carrinho.adicionarProduto(produto, 0));
        }

        @Test
        @DisplayName("Quando adicionar um produto nulo, então deve lançar exceção")
        void deveLancarExcecaoParaProdutoNulo() {
            assertThrows(NullPointerException.class,
                    () -> carrinho.adicionarProduto(null, 2));
        }

        @Test
        @DisplayName("Quando remover produto inexistente, então deve lançar exceção")
        void deveLancarExcecaoAoRemoverProdutoInexistente() {
            assertThrows(IllegalArgumentException.class,
                    () -> carrinho.removerProduto(produto));
        }
    }
}
