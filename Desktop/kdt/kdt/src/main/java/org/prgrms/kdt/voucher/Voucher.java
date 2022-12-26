package org.prgrms.kdt.voucher;

import java.util.UUID;

public interface Voucher {

    UUID getVoucherID();
    public long discount(long beforeDiscount);
}
