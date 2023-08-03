package com.boardify.boardify.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.boardify.boardify.entities.*;
import com.boardify.boardify.service.GameService;
import com.boardify.boardify.service.TournamentPlayerService;
import com.boardify.boardify.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import com.boardify.boardify.service.TournamentService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

class TournamentControllerTest {
    @Mock
    private TournamentService tournamentService;

    @Mock
    private GameService gameService;

    @Mock
    private UserService userService;

    @Mock
    private TournamentPlayerService tournamentPlayerService;

    @InjectMocks
    private TournamentController tournamentController;

    @Mock
    private Tournament tournament;

    @Mock
    private User user;

    @Mock
    private Authentication authentication;

    @Mock
    private TournamentPlayer tournamentPlayer;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;
    @Mock
    private Game game;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        tournamentController = new TournamentController(tournamentService, gameService, userService, tournamentPlayerService);
    }

    @Test
    void testShowEditTournamentPageWithExistingTournament() {
        // Arrange
        Long tournamentId = 5L;
        Tournament tournament = new Tournament();
        tournament.setTournamentId(tournamentId);

        when(tournamentService.findTournamentByID(tournamentId)).thenReturn(Optional.of(tournament));

        // Act
        String viewName = tournamentController.showEditTournamentPage(tournamentId, model);

        // Assert
        assertEquals("edit-tournament", viewName);
        // Verify that the model contains the tournament attribute
        verify(model).addAttribute("tournament", tournament);
        // Verify that there are no more interactions with the model
//        verifyNoMoreInteractions(model);
        // Log the success message
        System.out.println("Test testShowEditTournamentPageWithExistingTournament passed successfully.");

    }

    @Test
    void testShowEditTournamentPageWithNonExistingTournament() {
        // Arrange
        Long tournamentId = 200L;

        when(tournamentService.findTournamentByID(tournamentId)).thenReturn(Optional.empty());

        // Act
        String viewName = tournamentController.showEditTournamentPage(tournamentId, model);

        // Assert
        assertEquals("redirect:/tournament-to-edit", viewName);
        // Verify that there are no interactions with the model
        verifyNoInteractions(model);
        // Log the success message
        System.out.println("Test testShowEditTournamentPageWithNonExistingTournament passed successfully.");
    }
    @Test
    void testDeleteTournament() {
        // Arrange
        Long tournamentId = 22L;

        // Act
        String result = tournamentController.deleteTournament(tournamentId);

        // Assert
        assertEquals("redirect:/join-tournament", result);

        // Verify that the tournamentService.deleteTournament method was called with the correct tournamentId
        verify(tournamentService).deleteTournament(tournamentId);

        // Verify that there are no more interactions with the mock objects
        verifyNoMoreInteractions(tournamentService, model);

        // Log the success message
        System.out.println("Test testDeleteTournament passed successfully.");
    }

    @Test
    void testCreateTournament_Success() {
        // Arrange
        Long gameId = 1L;
        String email = "kjenneryz@aboutads.info";
        Tournament tournament = new Tournament();
        tournament.setTournamentId(111L);
        User user = new User();
        user.setEmail(email);

        // Mocking the relevant methods
        when(gameService.findGameById(gameId)).thenReturn(new Game());
        when(userService.findByEmail(email)).thenReturn(user);
        when(tournamentService.createTournament(tournament)).thenReturn(new Tournament());

        // Act
        String result = tournamentController.createTournament(tournament, bindingResult, gameId, model);

        // Assert
        assertEquals("redirect:/", result);
        verify(gameService).findGameById(gameId);
        verify(userService).findByEmail(email);
        verify(tournamentService).createTournament(tournament);
        verify(model).addAttribute("errorMessage", "Success!");

        // Verify that there are no more interactions with the mock objects
        verifyNoMoreInteractions(gameService, userService, tournamentService, model);

        // Log the success message
        System.out.println("Test testCreateTournament_Success passed successfully.");
    }

    @Test
    void testCreateTournament_Failure() {
        // Arrange
        Long gameId = 1L;
        String email = "ed";
        Tournament tournament = new Tournament();
        User user = new User();
        user.setEmail(email);

        // Mocking the relevant methods
        //when(gameService.findGameById(gameId)).thenReturn(new Game());
       // when(userService.findByEmail(email)).thenReturn(user);
        when(tournamentService.createTournament(tournament)).thenThrow(new RuntimeException("Failed to create the tournament"));

        // Act
        String result = tournamentController.createTournament(tournament, bindingResult, gameId, model);

        // Assert
        assertEquals("create-tournament", result);
       // verify(gameService).findGameById(gameId);
       // verify(userService).findByEmail(email);
        verify(tournamentService).createTournament(tournament);
        verify(model).addAttribute("errorMessage", "Failed to create the tournament");

        // Verify that there are no more interactions with the mock objects
        verifyNoMoreInteractions(gameService, userService, tournamentService, model);

        // Log the success message
        System.out.println("Test testCreateTournament_Failure passed successfully.");
    }

    @Test
    void testCreateTournament_ValidationErrors() {
        // Arrange
        Long gameId = 1L;
        String email = "edjos@gmail.com";
        Tournament tournament = new Tournament();
        User user = new User();
        user.setEmail(email);

        // Simulate validation errors
        when(bindingResult.hasErrors()).thenReturn(true);
        List<FieldError> errors = new ArrayList<>();
        errors.add(new FieldError("tournament", "name", "Name is required"));
        when(bindingResult.getFieldErrors()).thenReturn(errors);

        // Act
        String result = tournamentController.createTournament(tournament, bindingResult, gameId, model);

        // Assert
        assertEquals("create-tournament", result);
        verify(gameService).findGameById(gameId);
        verify(userService).findByEmail(email);

        // Verify that there are no more interactions with the mock objects
        verifyNoMoreInteractions(gameService, userService, tournamentService, model);

        // Log the success message
        System.out.println("Test testCreateTournament_ValidationErrors passed successfully.");
    }

    @Test
    void testEditRating() {
        // Arrange
        Long tournamentId = 22L;
        Long userId = 36L;
        Integer tournamentRating = 4;
        Integer organizerRating = 4;
        // Create a mock TournamentPlayer object
        TournamentPlayer ratingObj = new TournamentPlayer();
        ratingObj.setTournamentRating(tournamentRating);
        ratingObj.setOrganizerRating(organizerRating);

        // Create a mock User object
        User user = new User();
        user.setId(userId);

        // Set up the Authentication mock
       // when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("kjenneryz@aboutads.info");

        // Mock the UserService
        when(userService.findByEmail(anyString())).thenReturn(user);

        // Create a mock TournamentPlayerService
        TournamentPlayerKey key = new TournamentPlayerKey(tournamentId, userId);
        when(tournamentPlayerService.findTournamentPlayerByKey(key)).thenReturn(Optional.of(tournamentPlayer));

        // Set up the savePlayerRating mock
        doNothing().when(tournamentPlayerService).savePlayerRating(any());

        // Act
        String result = tournamentController.editRating(tournamentId, ratingObj);

        // Assert
        assertEquals("redirect:/join-tournament", result);

        // Verify that the necessary methods were called with the correct arguments
        verify(authentication, times(1)).isAuthenticated();
        verify(authentication).getName();
        verify(userService).findByEmail(anyString());
        verify(tournamentPlayerService).findTournamentPlayerByKey(key);

        // Verify that the setter methods were called on the TournamentPlayer object
        verify(tournamentPlayer).setTournamentRating(tournamentRating);
        verify(tournamentPlayer).setOrganizerRating(organizerRating);

        // Verify that the savePlayerRating method was called with the TournamentPlayer object
        verify(tournamentPlayerService).savePlayerRating(tournamentPlayer);

        // Verify that there are no more interactions with the mock objects
        verifyNoMoreInteractions(authentication, userService, tournamentPlayerService, tournamentPlayer);

        // Log the success message
        System.out.println("Test testEditRating passed successfully.");
    }
}