package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.form.Order;
import com.form.OrderItem;
import com.form.OrderItemExample;
import com.form.Product;
import com.mapper.OrderItemMapper;

@Service
public class OrderItemServiceImpl implements OrderItemService{

	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private ProductService productService;
	
	@Override
	public OrderItem getById(Integer id) {
		OrderItem orderItem = orderItemMapper.selectByPrimaryKey(id);
		setProduct(orderItem);
		return orderItem;
	}
	public void setProduct(List<OrderItem> ois) {
		for (OrderItem oi : ois) {
			setProduct(oi);
		}
	}
	private void setProduct(OrderItem oi) {
		Product p = productService.get(oi.getProduct_id());
		oi.setProduct(p);
	}
	
	@Override
	public void add(OrderItem orderItem) {
		orderItemMapper.insert(orderItem);
	}

	@Override
	public List<OrderItem> getByOrderId(Integer order_id) {
		OrderItemExample example = new OrderItemExample();
		example.or().andOrder_idEqualTo(order_id);
		List<OrderItem> result = orderItemMapper.selectByExample(example);
		setProduct(result);
		return result;
	}

	@Override
	public void update(OrderItem orderItem) {
		orderItemMapper.updateByPrimaryKey(orderItem);
	}

	@Override
	public List<OrderItem> listByUserId(Integer user_id) {
		OrderItemExample example = new OrderItemExample();
		example.or().andUser_idEqualTo(user_id);
		List<OrderItem> result = orderItemMapper.selectByExample(example);
		setProduct(result);
		return result;
	}

	@Override
	public List<OrderItem> listForCart(Integer user_id) {
		OrderItemExample example = new OrderItemExample();
		example.or().andUser_idEqualTo(user_id).andOrder_idIsNull();
		List<OrderItem> result = orderItemMapper.selectByExample(example);
		setProduct(result);
		return result;
	}

	@Override
	public void delete(Integer id) {
		orderItemMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void fill(List<Order> orders) {
		for (Order order : orders) {
			fill(order);
		}
	}

	@Override
	public void fill(Order order) {
		OrderItemExample example = new OrderItemExample();
		example.or().andOrder_idEqualTo(order.getId());
		example.setOrderByClause("id desc");
		List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
		setProduct(orderItems);

		float total = 0;
		int totalNumber = 0;
		for (OrderItem oi : orderItems) {
			total += oi.getNumber() * oi.getProduct().getPrice();
			totalNumber += oi.getNumber();
		}
		order.setTotal(total);
		order.setTotalNumber(totalNumber);
		order.setOrderItems(orderItems);
	}

}
