package org.prgrms.kdt;

import org.prgrms.kdt.order.OrderProperties;
import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.JdvcVoucherRepository;
import org.prgrms.kdt.voucher.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.text.MessageFormat;
import java.util.UUID;

@SpringBootApplication
//@ComponentScan(
//		basePackages = {"org.prgrms.kdt.order", "org.prgrms.kdt.voucher"}
//)
public class KdtApplication {
	private static final Logger logger = LoggerFactory.getLogger(KdtApplication.class);

	public static void main(String[] args) {

		//SpringApplication.run(KdtApplication.class, args);

		var applicationContext = new AnnotationConfigApplicationContext(KdtApplication.class);
		var orderProperties = applicationContext.getBean(OrderProperties.class);

		logger.error("logger name => {}", logger.getName());
		logger.warn("version ->{}", orderProperties.getVersion());
		logger.warn("minimumOrderAmount ->{}", orderProperties.getMinimumOrderAmount());
		logger.warn("supportVendors ->{}", orderProperties.getSupportVendors());
		logger.warn("description ->{}", orderProperties.getDescription());
	}

}
