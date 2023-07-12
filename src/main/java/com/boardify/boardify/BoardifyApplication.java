package com.boardify.boardify;

import com.boardify.boardify.entities.Subscription;
import com.boardify.boardify.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.util.Arrays;
import java.util.List;


@PropertySource("classpath:application.properties")
@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories("com.boardify.boardify.repository")
public class BoardifyApplication extends SpringBootServletInitializer implements ApplicationRunner {

	@Autowired
	private SubscriptionService subscriptionService;

	public static void main(String[] args) {
		SpringApplication.run(BoardifyApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<Subscription> subscriptions = subscriptionService.findAllSubscriptions();

		if(subscriptions != null && subscriptions.isEmpty()) {

			Subscription sub1 = Subscription.builder().subscriptionID(Long.valueOf(1)).subscriptionName("Premium").cost(15).duration("1 Month").commissionRate(0.05).build();
			Subscription sub2 = Subscription.builder().subscriptionID(Long.valueOf(2)).subscriptionName("Premium").cost(150).duration("1 Year").commissionRate(0.05).build();

			Arrays.asList(sub1, sub2).forEach(s -> subscriptionService.createSubscription(s));

			System.out.println("New subscriptions added to database");
		}
	}
}
