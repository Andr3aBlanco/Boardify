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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@PropertySource("classpath:application.properties")
@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories("com.boardify.boardify.repository")
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
		CreateDefaultSubscriptions();
		CreateDefaultRoles();
		CreateInitialUsers();
	}

	public void CreateInitialUsers() {
		List<UserDto> usersDto = userService.findAllUsers();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if(usersDto != null && usersDto.isEmpty()) {
			List<Role> roles = roleRepository.findAll();

			List<Role> adminRoles = new ArrayList<Role>();
			adminRoles.add(roles.get(0));
			User user1 = new User(Long.valueOf(1), "Thanos", "admin@admin.com", passwordEncoder.encode("#admin"), "Okay", adminRoles,
					"Thanos", "Power Glover", "1337 Street", "Delta", "Canada", "British Columbia", "V4N 4V4", "778-778-7788", null, 0, null);

			List<Role> basicRoles = new ArrayList<Role>();
			basicRoles.add(roles.get(1));
			User user2 = new User(Long.valueOf(2), "Captain America", "basic@basic.com", passwordEncoder.encode("#basic"), "Okay", basicRoles,
					"Steve", "Rogers", "1337 Street", "Surrey", "Canada", "British Columbia", "V4N 4V4", "778-778-7788", null, 0, null);

			List<Role> premiumRoles = new ArrayList<Role>();
			premiumRoles.add(roles.get(2));
			User user3 = new User(Long.valueOf(3), "Iron Man", "premium@premium.com", passwordEncoder.encode("#premium"), "Okay", premiumRoles,
					"Tony", "Stark", "1337 Street", "Vancouver", "Canada", "British Columbia", "V4N 4V4", "778-778-7788", null, 2, null);

			User user4 = new User(Long.valueOf(4), "Batman", "batman@batman.com", passwordEncoder.encode("#batman"), "Okay", basicRoles,
					"Bruce", "Wayne", "1337 Street", "Gotham City", "United States", "New Jersey", "I'm Batman", "778-778-7788", null, 0, null);
			System.out.println("Initial users added to database");

			Arrays.asList(user1, user2, user3, user4).forEach(user -> userRepository.save(user));
		}
	}

	public void CreateDefaultSubscriptions() {
		List<Subscription> subscriptions = subscriptionService.findAllSubscriptions();

		if(subscriptions != null && subscriptions.isEmpty()) {

			Subscription sub1 = Subscription.builder().subscriptionID(Long.valueOf(1)).subscriptionName("Premium").cost(15).duration("1 Month").commissionRate(0.05).build();
			Subscription sub2 = Subscription.builder().subscriptionID(Long.valueOf(2)).subscriptionName("Premium").cost(150).duration("1 Year").commissionRate(0.05).build();

			Arrays.asList(sub1, sub2).forEach(s -> subscriptionService.createSubscription(s));

			System.out.println("Default subscriptions added to database");
		}
	}

	public void CreateDefaultRoles() {
		List<Role> roles = roleRepository.findAll();

		if(roles != null && roles.isEmpty()) {
			Role admin = new Role();
			Role basic = new Role();
			Role premium = new Role();

			admin.setId(Long.valueOf(1));
			admin.setName("ROLE_ADMIN");

			basic.setId(Long.valueOf(2));
			basic.setName("ROLE_BASIC");

			premium.setId(Long.valueOf(3));
			premium.setName("ROLE_PREMIUM");

			Arrays.asList(admin, basic, premium).forEach(role -> roleRepository.save(role));
			System.out.println("Default roles added to database");
		}
	}
}
