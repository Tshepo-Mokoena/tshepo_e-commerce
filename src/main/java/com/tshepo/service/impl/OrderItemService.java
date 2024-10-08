package com.tshepo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Order;
import com.tshepo.persistence.OrderItem;
import com.tshepo.persistence.repository.IOrderItemRepository;
import com.tshepo.service.IOrderItemService;

@Service
public class OrderItemService implements IOrderItemService{
	
	private IOrderItemRepository orderItemRepository;
	
	@Autowired
	private void setOrderItemService(IOrderItemRepository orderItemRepository) 
	{
		this.orderItemRepository = orderItemRepository;
	}

	@Transactional
	@Override
	public OrderItem newOrderItem(OrderItem orderItem) 
	{
		orderItem.setCreatedAt(LocalDateTime.now());
		orderItem.setUpdatedAt(LocalDateTime.now());
		return orderItemRepository.save(orderItem);
	}

	@Override
	public List<OrderItem> findByOrder(Order order) 
	{
		return orderItemRepository.findByOrder(order);
	}


}
