package org.prgrms.kdt.aop;

import org.aspectj.lang.annotation.Pointcut;

public class CommonPointcut {

    @Pointcut("execution(public * org.prgrms.kdt..*Service.*(..))")
    public void servicePublicMethodPointcut(){

    }

    @Pointcut("execution(public * org.prgrms.kdt..*Repositry.*(..))")
    public void repositoryMethodPointcut(){

    }

    @Pointcut("execution(public * org.prgrms.kdt..*Repository.insert(..))")
    public void repositoryInsertMethodPointcut(){

    }
}
