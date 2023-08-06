package com.boardify.boardify.controller;

import com.boardify.boardify.DTO.UserDto;
import com.boardify.boardify.entities.Subscription;
import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.entities.Transaction;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.service.*;
import com.boardify.boardify.utilities.Response;
import com.stripe.model.Coupon;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Controller
public class PaymentController {

	@Autowired
	private SubscriptionService subscriptionService;

	@Autowired
	private TournamentService tournamentService;

	@Autowired
	TransactionService transactionService;

	@Value("${stripe.key.public}")
	private String API_PUBLIC_KEY;

	private UserService userService;
	private StripeService stripeService;

	public PaymentController(StripeService stripeService, UserService userService) {
		this.stripeService = stripeService;
		this.userService = userService;
	}


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

		int entryFeesInt = (int) Math.round(entryFees);

		Optional<Tournament> tournamentOptional = tournamentService.findTournamentByID(tournamentId);
		if (tournamentOptional.isPresent()) {
			Tournament tournament = tournamentOptional.get();

			// Add the tournament ID, user ID, entry fees, and tournament name to the model
			model.addAttribute("tournamentId", tournamentId);
			model.addAttribute("userId", userId);
			model.addAttribute("entryFees", entryFeesInt);
			model.addAttribute("tournamentName", tournament.getTournamentName());
			model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
		} else {

			model.addAttribute("errorMessage", "Tournament not found");
			return "error";
		}

		// Return the view name for the "payentryfees" template
		return "payentryfees";
	}


	@PostMapping("/create-charge")
	public @ResponseBody Response createCharge(String email,
											   String token,
											   int amount,
											   Long tourid,
											   Long userid) {

		System.out.println("This is the tournament ID " + tourid + "    AHA");
		if (token == null) {
			return new Response(true, "Stripe payment token is missing. please try again later.");
		}

		String chargeId = stripeService.createCharge(email, token, amount);//

		if (chargeId == null) {
			return new Response(false, "An error occurred while trying to charge.");
		}

		// Find the tournament
		Optional<Tournament> tournament = tournamentService.findTournamentByID(tourid);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String userEmail = userDetails.getUsername();
		User user = userService.findByEmail(userEmail);
		// Record the transaction in the database
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setCommission(0); //
		transaction.setItem("Entry Fees"); //
		transaction.setTransactionDate(LocalDate.now());
		transaction.setTransactionTime(LocalTime.now());
		transaction.setTransactionType(String.valueOf(3)); // 3 for entry fees
		transaction.setTournament(tournament.get()); //
		transaction.setUser(user);

		transactionService.SaveTransaction(transaction);

		return new Response(true, "Success your charge id is " + chargeId);
	}

	@PostMapping("/create-charge2")
	public @ResponseBody Response createCharge2(String email, String token, int amount, int subscriptionType) {

		if (token == null) {
			return new Response(false, "Stripe payment token is missing. Please try again later.");
		}

		String chargeId = stripeService.createCharge(email, token, amount); //

		if (chargeId == null) {
			return new Response(false, "An error occurred while trying to charge.");
		}

		// Find the tournament
//		Optional<Tournament> tournament = tournamentService.findTournamentByID(tournamentId);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String userEmail = userDetails.getUsername();
		User user = userService.findByEmail(userEmail);
		// Record the transaction in the database
		Transaction transaction = new Transaction();
		transaction.setAmount(amount/100);
		transaction.setCommission(0); // You might need to calculate commission here
		transaction.setItem("Premium Subscription"); // Change as needed
		transaction.setTransactionDate(LocalDate.now());
		transaction.setTransactionTime(LocalTime.now());
		transaction.setTransactionType(String.valueOf(subscriptionType)); // 1 for month plan, 2 for year plan
		transaction.setTournament(null); // Set to null if not applicable
		transaction.setUser(user);

		// Save the transaction in the database using your transaction service
		 transactionService.SaveTransaction(transaction);

		// You may want to store charge id along with order information

		return new Response(true, "Success, your charge id is " + chargeId);
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
