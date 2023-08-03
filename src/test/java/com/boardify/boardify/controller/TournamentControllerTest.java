package com.boardify.boardify.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.boardify.boardify.entities.TournamentPlayer;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.service.GameService;
import com.boardify.boardify.service.TournamentPlayerService;
import com.boardify.boardify.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.service.TournamentService;
import org.springframework.validation.BindingResult;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    void handleNull() {
    }

    @Test
    void getTournament() {
    }

    @Test
    void showEditTournamentPage() {

    }

    @Test
    void editTournament() {
    }

    @Test
    void deleteTournament() {
    }

    @Test
    void createTournament() {
    }

    @Test
    void editRating() {
    }
}