package com.boardify.boardify;

import com.boardify.boardify.DTO.UserDto;
import com.boardify.boardify.entities.Role;
import com.boardify.boardify.entities.Subscription;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.repository.RoleRepository;
import com.boardify.boardify.repository.UserRepository;
import com.boardify.boardify.service.SubscriptionService;
import com.boardify.boardify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@PropertySource("classpath:application.properties")

@SpringBootApplication
public class BoardifyApplication extends SpringBootServletInitializer implements ApplicationRunner {

	@Autowired
	private SubscriptionService subscriptionService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;
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

		List<Role> roles = roleRepository.findAll();

		Role admin = new Role();
		Role basic = new Role();
		Role premium = new Role();
		if(roles != null && roles.isEmpty()) {

			admin.setId(Long.valueOf(1));
			admin.setName("ROLE_ADMIN");


			basic.setId(Long.valueOf(2));
			basic.setName("ROLE_BASIC");


			premium.setId(Long.valueOf(3));
			premium.setName("ROLE_PREMIUM");

			Arrays.asList(admin, basic, premium).forEach(role -> roleRepository.save(role));
			System.out.println("New roles added to database");
		}



		List<UserDto> usersDto = userService.findAllUsers();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if(usersDto != null && usersDto.isEmpty()) {

			User user1 = new User();
			user1.setId(Long.valueOf(1));
			user1.setEmail("admin@admin.com");
			user1.setName("admin admin");
			List<Role> adminRoles = new ArrayList<Role>();
			adminRoles.add(admin);
			user1.setRoles(adminRoles);
			user1.setPassword(passwordEncoder.encode("#admin"));


			User user2 = new User();
			user2.setId(Long.valueOf(2));
			user2.setEmail("basic@basic.com");
			user2.setName("basic basic");
			List<Role> basicRoles = new ArrayList<Role>();
			basicRoles.add(basic);
			user2.setRoles(basicRoles);
			user2.setPassword(passwordEncoder.encode("#basic"));

			User user3 = new User();
			user3.setId(Long.valueOf(3));
			user3.setEmail("premium@premium.com");
			user3.setName("premium premium");
			List<Role> premiumRoles = new ArrayList<Role>();
			premiumRoles.add(premium);
			user3.setRoles(premiumRoles);
			user3.setPassword(passwordEncoder.encode("#premium"));

			Arrays.asList(user1, user2, user3).forEach(user -> userRepository.save(user));
			System.out.println("New users added to database");

		}
	}
}
