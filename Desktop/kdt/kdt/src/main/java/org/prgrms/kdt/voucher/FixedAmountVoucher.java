package org.prgrms.kdt.voucher;

import java.util.UUID;

public class FixedAmountVoucher implements Voucher {
    private static final long MAX_VOUHCER_AMOUNT = 10000;
    public final UUID voucherId;
    public final long amount;
    public FixedAmountVoucher(UUID voucherId, long amount){
        if(amount<0) throw new IllegalArgumentException("Amount should be positive");
        if(amount==0) throw new IllegalArgumentException("Amount should be not be zero");
        if(amount> MAX_VOUHCER_AMOUNT) throw new IllegalArgumentException("Amount should be less than %d".formatted(MAX_VOUHCER_AMOUNT));
        this.voucherId = voucherId;
        this.amount = amount;
    }

    @Override
    public UUID getVoucherID() {
        return this.voucherId;
    }

    public long discount(long beforeDiscount){
        var discountedAmount =  beforeDiscount - amount;

        return (discountedAmount < 0) ? 0 : discountedAmount;
    }
}
