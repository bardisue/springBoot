package org.prgrms.kdt;


import org.prgrms.kdt.io.Out;

public class Main {
    public static void main(String[] args){
        Out out = new Out() {
            @Override
            public void startMessage() {
                Out.super.startMessage();
            }
        };
        out.startMessage();
    }
}
