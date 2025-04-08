package com.algaworks.junit.ecommerce;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CarrinhoCompra {

	private final Cliente cliente;
	private final List<ItemCarrinhoCompra> itens;

	public CarrinhoCompra(Cliente cliente) {
		this(cliente, new ArrayList<>());
	}

	public CarrinhoCompra(Cliente cliente, List<ItemCarrinhoCompra> itens) {
		Objects.requireNonNull(cliente);
		Objects.requireNonNull(itens);
		this.cliente = cliente;
		this.itens = new ArrayList<>(itens); // Cria lista caso passem uma imutável
	}

	public List<ItemCarrinhoCompra> getItens() {
		return new ArrayList<>(this.itens); // Retorna cópia para evitar modificações externas
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void adicionarProduto(Produto produto, int quantidade) {
		Objects.requireNonNull(produto, "Produto não pode ser nulo");
		if (quantidade < 1) {
			throw new IllegalArgumentException("Quantidade deve ser maior ou igual a 1");
		}

		Optional<ItemCarrinhoCompra> itemExistente = itens.stream()
				.filter(i -> i.getProduto().equals(produto))
				.findFirst();

		if (itemExistente.isPresent()) {
			itemExistente.get().adicionarQuantidade(quantidade);
		} else {
			itens.add(new ItemCarrinhoCompra(produto, quantidade));
		}
	}

	public void removerProduto(Produto produto) {
		Objects.requireNonNull(produto, "Produto não pode ser nulo");

		boolean removido = itens.removeIf(i -> i.getProduto().equals(produto));
		if (!removido) {
			throw new IllegalArgumentException("Produto não encontrado no carrinho");
		}
	}

	public void aumentarQuantidadeProduto(Produto produto) {
		Objects.requireNonNull(produto, "Produto não pode ser nulo");

		ItemCarrinhoCompra item = itens.stream()
				.filter(i -> i.getProduto().equals(produto))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Produto não encontrado no carrinho"));

		item.adicionarQuantidade(1);
	}

	public void diminuirQuantidadeProduto(Produto produto) {
		Objects.requireNonNull(produto, "Produto não pode ser nulo");

		ItemCarrinhoCompra item = itens.stream()
				.filter(i -> i.getProduto().equals(produto))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Produto não encontrado no carrinho"));

		if (item.getQuantidade() == 1) {
			itens.remove(item);
		} else {
			item.subtrairQuantidade(1);
		}
	}

	public BigDecimal getValorTotal() {
		return itens.stream()
				.map(ItemCarrinhoCompra::getValorTotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public int getQuantidadeTotalDeProdutos() {
		return itens.stream()
				.mapToInt(ItemCarrinhoCompra::getQuantidade)
				.sum();
	}

	public void esvaziar() {
		itens.clear();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CarrinhoCompra that = (CarrinhoCompra) o;
		return Objects.equals(itens, that.itens) && Objects.equals(cliente, that.cliente);
	}

	@Override
	public int hashCode() {
		return Objects.hash(itens, cliente);
	}
}