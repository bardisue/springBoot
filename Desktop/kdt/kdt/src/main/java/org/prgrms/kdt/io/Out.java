package org.prgrms.kdt.io;
public interface Out {
    default void startMessage(){
        System.out.println("""
                === Voucher Program ===
                Type exit to exit the program.
                Type create to create a new voucher.
                Type list to list all vouchers.""");
    }
}
