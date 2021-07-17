package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.table.dto.OrderTableRepository;
import kitchenpos.domain.OrderRequest;
import kitchenpos.domain.OrderLineItemRequest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuDao;
    private final OrderRepository orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableRepository orderTableDao;

    public OrderService(
            final MenuRepository menuDao,
            final OrderRepository orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableRepository orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderRequest create(final OrderRequest order) {
        final List<OrderLineItemRequest> orderLineItems = order.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTableRequest orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        final OrderRequest savedOrder = orderDao.save(order);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItemRequest> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemRequest orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    public List<OrderRequest> list() {
        final List<OrderRequest> orders = orderDao.findAll();

        for (final OrderRequest order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public OrderRequest changeOrderStatus(final Long orderId, final OrderRequest order) {
        final OrderRequest savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
