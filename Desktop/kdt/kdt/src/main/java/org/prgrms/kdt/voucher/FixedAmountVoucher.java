package org.prgrms.kdt.voucher;

import java.util.UUID;

public class FixedAmountVoucher implements Voucher {
    public final UUID voucherId;
    public final long amount;
    public FixedAmountVoucher(UUID voucherId, long amount){
        this.voucherId = voucherId;
        this.amount = amount;
    }

    @Override
    public UUID getVoucherID() {
        return this.voucherId;
    }

    public long discount(long beforeDiscount){
        return beforeDiscount - amount;
    }
}
