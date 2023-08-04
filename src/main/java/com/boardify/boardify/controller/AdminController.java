package com.boardify.boardify.controller;

import com.boardify.boardify.DTO.UserDto;
import com.boardify.boardify.entities.Transaction;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.service.TransactionService;
import com.boardify.boardify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    public AdminController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }
    @GetMapping("/transactions")
    public String showTransactionsPage(@RequestParam Map<String, String> customQuery, Model model) {
        List<Transaction> transactions;
        System.out.println(customQuery.get("item"));
        if (customQuery.size() < 1) {
            transactions = transactionService.findAllTransactions();
        } else {
            transactions = transactionService.findByFilter(customQuery);
        }
        model.addAttribute("transactions", transactions);
        return "transactions";
    }

    @GetMapping("/users")
    public String listRegisteredUsers(Model model){
        List<UserDto> users = userService.findAllNonAdmins();
        model.addAttribute("users", users);
        String[] accountStatuses = {"Okay", "Banned", "Locked"};
        model.addAttribute("accStatuses", accountStatuses);
        UserDto tempUser = new UserDto();
        model.addAttribute("tempUser", tempUser);
        return "users";
    }

    @PostMapping("/users/edit/{email}")
    public String changeUserAccountStatus(@PathVariable("email") String email, @ModelAttribute("tempUser") UserDto tempUser){
        userService.changeAccountStatus(email, tempUser.getAccountStatus());

        return "redirect:/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("email") String email) {
        // Fetch the user from the database based on the email
        User user = userService.findByEmail(email);

        // Delete the user from the database
        userService.deleteUserById(user.getId());

        return "redirect:/users"; // Redirect to the user list page
    }
}
