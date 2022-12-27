package org.prgrms.kdt.voucher;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FixedAmountVoucherTest {
    private static final Logger logger = LoggerFactory.getLogger(FixedAmountVoucherTest.class);

    @BeforeAll
    static void setup(){
        logger.info("@BeforeAll - 단 한번 실행");
    }

    @BeforeEach
    void init(){
        logger.info("@BeforeEach - 매 테스트마다 실행");
    }

    @Test
    @DisplayName("기본적인 assertEqual 테스트")
    void testAssertEqual(){
        assertEquals(2, 1+1);
    }
    @Test
    @DisplayName("주어진 금액만큼 할인을 해야한다")
    void discount() {
        var sut =new FixedAmountVoucher(UUID.randomUUID(), 100);
        assertEquals(900, sut.discount(1000));
    }

    @Test
    @DisplayName("디스카운트된 금액은 마이너스가 될 수 업ㅄ다.")
    void testMinusDiscountAmount() {
        var sut = new FixedAmountVoucher(UUID.randomUUID(), 1000);
        assertEquals(0, sut.discount(900));
    }

    @Test
    @DisplayName("할인금액은 마이너스가 될 수 없다.")
    //@Disabled
    void testWithMinus() {
        assertThrows(IllegalArgumentException.class, ()->new FixedAmountVoucher(UUID.randomUUID(), -100));
    }

    @Test
    @DisplayName("우효한 할인금액으로만 생성가능")
    void testVoucherCreation(){
        assertAll("FixedAmountVoucer creation",
                () -> assertThrows(IllegalArgumentException.class, ()-> new FixedAmountVoucher(UUID.randomUUID(), 0)),
                () -> assertThrows(IllegalArgumentException.class, ()-> new FixedAmountVoucher(UUID.randomUUID(), -100)),
                () -> assertThrows(IllegalArgumentException.class, ()-> new FixedAmountVoucher(UUID.randomUUID(), 1000000))
        );
    }
}
