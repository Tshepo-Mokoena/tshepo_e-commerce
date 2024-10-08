package com.tshepo.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
@Data
@Entity
@Table(name = "carts")
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "total")
	private BigDecimal total;
	
	@OneToMany(
			mappedBy = "cart", 
			cascade = CascadeType.ALL, 
			fetch=FetchType.LAZY
			)
	private List<CartItem> cartItems;
	
	@OneToOne(
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY
			)
	@JoinColumn(name = "account_id", nullable = false)
	@JsonIgnore
	private Account account;
	
	@Column(
			name = "updated_at",
			nullable = false
			)
	private LocalDateTime updatedAt;
	
	@Column(
			name = "created_at",
			nullable = false
			)
	private LocalDateTime createdAt;

	public static Cart setCart(BigDecimal total, Account account, LocalDateTime updatedAt, LocalDateTime createdAt) 
	{
		Cart cart = new Cart();
		cart.setAccount(account);
		cart.setUpdatedAt(updatedAt);
		cart.setCreatedAt(createdAt);
		cart.setTotal(total);		
		return cart;
	}
	
}