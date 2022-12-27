package org.prgrms.kdt.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.MemoryVoucherRepository;
import org.prgrms.kdt.voucher.Voucher;
import org.prgrms.kdt.voucher.VoucherService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    class OrderRepositoryStub implements OrderRepository{
        @Override
        public Order insert(Order order) {
            return null;
        }
    }
    @Test
    void createOrder() {
//          GIVEN
        var voucherRepository = new MemoryVoucherRepository();
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);
        var sut = new OrderService(new VoucherService(voucherRepository), new OrderRepositoryStub());

//          WHEN
        var order = sut.createOrder(UUID.randomUUID(), List.of(new OrderItem((UUID.randomUUID()), 200, 1)), fixedAmountVoucher.getVoucherID());

        //  THEN
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherID(), is(fixedAmountVoucher.getVoucherID()));
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCECPTED));
    }

    @Test
    @DisplayName("오더가 생서되어야한다. (mock)")
    void createOrderByMock() {
        //GIVE
        var voucherServiceMock = mock(VoucherService.class);
        var orderRepositoryMock = mock(OrderRepository.class);
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        when(voucherServiceMock.getVoucher(fixedAmountVoucher.getVoucherID())).thenReturn(fixedAmountVoucher);
        var sut = new OrderService(voucherServiceMock, orderRepositoryMock);

        //  WHEN
        var order = sut.createOrder(
                UUID.randomUUID(),
                List.of(new OrderItem(UUID.randomUUID(), 200,1)),
                fixedAmountVoucher.getVoucherID()
        );

        //  THEN
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        var inOrder = inOrder(voucherServiceMock, orderRepositoryMock);
        inOrder.verify(voucherServiceMock).getVoucher(fixedAmountVoucher.getVoucherID());
        inOrder.verify(orderRepositoryMock).insert(order);
        inOrder.verify(voucherServiceMock).useVoucher(fixedAmountVoucher);
    }
}
