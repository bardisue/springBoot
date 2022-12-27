package org.prgrms.kdt.order;

import org.prgrms.kdt.voucher.Voucher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Order {
    private final UUID orderId;
    private final UUID customerId;
    private final List<OrderItem> orderItems;
    private Optional<Voucher> voucher;
    private OrderStatus orderStatus = OrderStatus.ACCECPTED;

    public Order(UUID orderid, UUID customerId, List<OrderItem> orderItems, Voucher voucher) {
        this.orderId = orderid;
        this.customerId = customerId;
        this.orderItems = orderItems;
        this.voucher = Optional.of(voucher);
    }

    public Order(UUID orderid, UUID customerId, List<OrderItem> orderItems) {
        this.orderId = orderid;
        this.customerId = customerId;
        this.orderItems = orderItems;
        this.voucher = Optional.empty();
    }

    public long totalAmount(){
        var beforeDiscount = orderItems.stream().map(v -> v.getProductPrice()*v.getQuantity())
                .reduce(0L, Long::sum);
        return voucher.map(value -> value.discount(beforeDiscount)).orElse(beforeDiscount);
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public UUID getOrderid() {
        return orderId;
    }

    public Optional<Voucher> getVoucher() {
        return voucher;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
