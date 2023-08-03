package com.boardify.boardify.controller;

import com.boardify.boardify.DTO.UserDto;
import com.boardify.boardify.entities.Subscription;
import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.service.StripeService;
import com.boardify.boardify.service.SubscriptionService;
import com.boardify.boardify.service.TournamentService;
import com.boardify.boardify.service.UserService;
import com.boardify.boardify.utilities.Response;
import com.stripe.model.Coupon;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
public class PaymentController {

	@Autowired
	private SubscriptionService subscriptionService;

	@Autowired
	private TournamentService tournamentService;

	@Value("${stripe.key.public}")
	private String API_PUBLIC_KEY;

	private UserService userService;
	private StripeService stripeService;

	public PaymentController(StripeService stripeService, UserService userService) {
		this.stripeService = stripeService;
		this.userService = userService;
	}

//	@GetMapping("/")
//	public String homepage() {
//		return "homepage";
//	}

	@GetMapping("/subscription")
	public String subscriptionPage(Model model) {
		model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
		return "subscription";
	}

	@GetMapping("/go-premium")
	public String chargePage(Model model, HttpServletRequest request) {
//		        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Object obj = auth.getPrincipal();
//        model.addAttribute("testUser", obj);
//        UserDetails userDetails = (UserDetails) auth.getPrincipal();
//        String userEmail = userDetails.getUsername();
//        UserDto userDto = userService.convertEntityToDto(userService.findByEmail(userEmail));
//        model.addAttribute("currentUser", userDto);
		model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
		List<Subscription> subscriptions = subscriptionService.findAllSubscriptions();
		model.addAttribute("subscriptions", subscriptions);
		return "plans";
	}


	@GetMapping("/pay-entry-fees/{tournamentId}/{userId}")
	public String payEntryFees(
			@PathVariable Long tournamentId,
			@PathVariable Long userId,
			@RequestParam(name = "entryFees") Double entryFees,
			Model model
	) {
		Optional<Tournament> tournamentOptional = tournamentService.findTournamentByID(tournamentId);
		if (tournamentOptional.isPresent()) {
			Tournament tournament = tournamentOptional.get();

			// Add the tournament ID, user ID, entry fees, and tournament name to the model
			model.addAttribute("tournamentId", tournamentId);
			model.addAttribute("userId", userId);
			model.addAttribute("entryFees", entryFees);
			model.addAttribute("tournamentName", tournament.getTournamentName());
			model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
		} else {
			// Handle the case when the tournament with the given ID is not found in the database
			// You can redirect the user to an error page or display an error message
			// For example:
			model.addAttribute("errorMessage", "Tournament not found");
			return "error";
		}

		// Return the view name for the "payentryfees" template
		return "payentryfees";
	}


	@PostMapping("/create-subscription")
	public @ResponseBody Response createSubscription(String email, String token, String plan, String coupon) {

		if (token == null || plan.isEmpty()) {
			return new Response(false, "Stripe payment token is missing. Please try again later.");
		}

		String customerId = stripeService.createCustomer(email, token);

		if (customerId == null) {
			return new Response(false, "An error accurred while trying to create customer");
		}

		String subscriptionId = stripeService.createSubscription(customerId, plan, coupon);

		if (subscriptionId == null) {
			return new Response(false, "An error accurred while trying to create subscription");
		}

		return new Response(true, "Success! your subscription id is " + subscriptionId);
	}

	@PostMapping("/cancel-subscription")
	public @ResponseBody Response cancelSubscription(String subscriptionId) {

		boolean subscriptionStatus = stripeService.cancelSubscription(subscriptionId);

		if (!subscriptionStatus) {
			return new Response(false, "Faild to cancel subscription. Please try again later");
		}

		return new Response(true, "Subscription cancelled successfully");
	}

	@PostMapping("/coupon-validator")
	public @ResponseBody Response couponValidator(String code) {

		Coupon coupon = stripeService.retriveCoupon(code);

		if (coupon != null && coupon.getValid()) {
			String details = (coupon.getPercentOff() == null ? "$" + (coupon.getAmountOff() / 100)
					: coupon.getPercentOff() + "%") + "OFF" + coupon.getDuration();
			return new Response(true, details);
		}
		return new Response(false, "This coupon code is not available. This may be because it has expired or has "
				+ "already been applied to your account.");
	}

	@PostMapping("/create-charge")
	public @ResponseBody Response createCharge(String email, String token, int amount) {

		if (token == null) {
			return new Response(false, "Stripe payment token is missing. please try again later.");
		}

		String chargeId = stripeService.createCharge(email, token, amount);// 9.99 usd

		if (chargeId == null) {
			return new Response(false, "An error accurred while trying to charge.");
		}

		// You may want to store charge id along with order information

		return new Response(true, "Success your charge id is " + chargeId);
	}

	@ModelAttribute("currentUser")
	public UserDto getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			String email = authentication.getName();
			User currentUser = userService.findByEmail(email);
			if (currentUser != null) {
				UserDto userDto = new UserDto();
				userDto.setUsername(currentUser.getUsername());
				userDto.setFirstName(currentUser.getFirstName());
				return userDto;
			}
		}
		return null;
	}
}