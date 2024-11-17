package com.alpaca.rankify.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.alpaca.rankify.MainActivity
import com.alpaca.rankify.presentation.panel.principal.destinations.home.create_ranking.CreateRankingViewModel
import com.alpaca.rankify.presentation.panel.principal.destinations.home.search_ranking.SearchRankingViewModel
import com.alpaca.rankify.util.RequestState
import com.alpaca.rankify.util.constant.TestingTagsConstants.CreateRanking.CREATE_RANKING_BUTTON
import com.alpaca.rankify.util.constant.TestingTagsConstants.CreateRanking.RANKING_NAME_TEXT_FIELD
import com.alpaca.rankify.util.constant.TestingTagsConstants.CreateRanking.RANKING_PASSWORD_TEXT_FIELD
import com.alpaca.rankify.util.constant.TestingTagsConstants.Navigation.HOME_NAV_BUTTON
import com.alpaca.rankify.util.constant.TestingTagsConstants.Navigation.MY_RANKINGS_NAV_BUTTON
import com.alpaca.rankify.util.constant.TestingTagsConstants.Ranking.CREATE_RANKING_PANEL
import com.alpaca.rankify.util.constant.TestingTagsConstants.Ranking.MY_RANKINGS_PANEL
import com.alpaca.rankify.util.constant.TestingTagsConstants.Ranking.RANKING_DETAILS_PANEL
import com.alpaca.rankify.util.constant.TestingTagsConstants.Ranking.SEARCH_RANKING_PANEL
import com.alpaca.rankify.util.constant.TestingTagsConstants.Ranking.Tabs.CREATE_RANKING_TAB
import com.alpaca.rankify.util.constant.TestingTagsConstants.Ranking.Tabs.SEARCH_RANKING_TAB
import com.alpaca.rankify.util.constant.TestingTagsConstants.SearchRanking.RANKING_ID_TEXT_FIELD
import com.alpaca.rankify.util.constant.TestingTagsConstants.SearchRanking.SEARCH_RANKING_BUTTON
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * This class contains UI tests for verifying the navigation behavior within the application.
 *
 * It uses Hilt for dependency injection and Compose for UI testing.
 * The tests cover various scenarios, such as navigating between different screens
 * and verifying the display of specific UI elements upon navigation.
 *
 * Key features:
 * - Uses Hilt for dependency injection.
 * - Uses Compose for UI testing.
 * - Covers navigation scenarios using Jetpack Compose Navigation.
 * - Asserts the display of UI elements based on navigation events.
 * - Uses MockK for mocking ViewModels in certain test cases.
 */
@HiltAndroidTest
class NavigationTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    private lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            SetupNavGraph(navController = navController)
        }
    }

    /**
     * This test verifies that the Create Ranking Panel is displayed as the initial screen
     * when the app is launched. It asserts that the composable with the tag
     * `CREATE_RANKING_PANEL` is visible.
     */
    @Test
    fun shouldShowCreateRankingPanelAsStartDestination() {
        composeTestRule
            .onNodeWithTag(CREATE_RANKING_PANEL)
            .assertIsDisplayed()
    }

    /**
     * This test verifies that the Search Ranking Panel is displayed when the Search Ranking Tab is clicked.
     *
     * **Scenario:**
     * 1. The user clicks on the Search Ranking Tab.
     * 2. The Search Ranking Panel should become visible.
     *
     * **Assertions:**
     * - Checks if the Search Ranking Panel is displayed using `assertIsDisplayed()`.
     */
    @Test
    fun shouldShowSearchRankingPanel_whenSearchTabClicked() {
        with(composeTestRule) {
            onNodeWithTag(SEARCH_RANKING_TAB)
                .performClick()
            onNodeWithTag(SEARCH_RANKING_PANEL)
                .assertIsDisplayed()
        }
    }

    /**
     * This test verifies that the Create Ranking Panel is displayed when the Create Ranking Tab is clicked.
     *
     * It first clicks on the Search Ranking Tab to ensure the app starts in a predictable state.
     * Then it clicks on the Create Ranking Tab.
     * Finally, it asserts that the Create Ranking Panel is displayed, indicating the successful navigation.
     */
    @Test
    fun shouldShowCreateRankingPanel_whenCreateRankingTabClicked() {
        with(composeTestRule) {
            onNodeWithTag(SEARCH_RANKING_TAB)
                .performClick()
            onNodeWithTag(CREATE_RANKING_TAB)
                .performClick()
            onNodeWithTag(CREATE_RANKING_PANEL)
                .assertIsDisplayed()
        }
    }

    /**
     * This test verifies that the My Rankings panel is displayed when the My Rankings button is clicked.
     *
     * It simulates a user interaction by clicking the My Rankings button and then asserts that
     * the My Rankings panel becomes visible.
     *
     * **Steps:**
     * 1. Find the My Rankings navigation button using its tag.
     * 2. Perform a click action on the button.
     * 3. Find the My Rankings panel using its tag.
     * 4. Assert that the My Rankings panel is displayed.
     */
    @Test
    fun shouldShowMyRankingsPanel_whenMyRankingsButtonClicked() {
        with(composeTestRule) {
            onNodeWithTag(MY_RANKINGS_NAV_BUTTON)
                .performClick()
            onNodeWithTag(MY_RANKINGS_PANEL)
                .assertIsDisplayed()
        }
    }

    /**
     * This test verifies that the Create Ranking Panel is displayed when the Home button is clicked.
     *
     * It follows these steps:
     * 1. Navigates to the My Rankings screen by clicking the My Rankings navigation button.
     * 2. Asserts that the My Rankings Panel is displayed, confirming successful navigation.
     * 3. Clicks the Home navigation button.
     * 4. Asserts that the Create Ranking Panel is displayed, verifying the expected behavior.
     */
    @Test
    fun shouldShowCreateRankingPanel_whenHomeButtonClicked() {
        with(composeTestRule) {
            onNodeWithTag(MY_RANKINGS_NAV_BUTTON)
                .performClick()
            onNodeWithTag(MY_RANKINGS_PANEL)
                .assertIsDisplayed()
            onNodeWithTag(HOME_NAV_BUTTON)
                .performClick()
            onNodeWithTag(CREATE_RANKING_PANEL)
                .assertIsDisplayed()
        }
    }

    /**
     * This test case verifies the navigation to the ranking details screen
     * when a new ranking is successfully created.
     *
     * It sets up a mock ViewModel with a successful ranking creation response
     * (RequestState.Success with a ranking ID). It then simulates user input
     * by entering a ranking name and password, and clicking the create button.
     *
     * Finally, it asserts that the ranking details panel is displayed,
     * indicating successful navigation.
     */
    @Test
    fun shouldNavigateToRankingDetails_whenRankingIsCreated() {
        val mockViewModel = mockk<CreateRankingViewModel>(relaxed = true)
        every { mockViewModel.rankingRequestState } returns MutableStateFlow(RequestState.Success(1L))
        with(composeTestRule) {
            onNodeWithTag(RANKING_NAME_TEXT_FIELD)
                .performTextInput("Ranking Teste")
            onNodeWithTag(RANKING_PASSWORD_TEXT_FIELD)
                .performTextInput("admin123")
            onNodeWithTag(CREATE_RANKING_BUTTON)
                .performClick()
            onNodeWithTag(RANKING_DETAILS_PANEL)
                .assertIsDisplayed()
        }
    }

    /**
     * This test verifies that the navigation to the Ranking Details screen does not occur
     * when there is invalid input during the ranking creation process.
     *
     * It simulates an error state in the CreateRankingViewModel by setting the
     * rankingRequestState to RequestState.Error.
     *
     * Then, it performs a click on the Create Ranking panel and asserts that
     * the Ranking Details panel does not exist, indicating that the navigation did not happen.
     */
    @Test
    fun shouldNotNavigateToRankingDetails_whenRankingCreationHasInvalidInput() {
        val mockViewModel = mockk<CreateRankingViewModel>(relaxed = true)
        every { mockViewModel.rankingRequestState } returns MutableStateFlow(RequestState.Error(""))
        with(composeTestRule) {
            onNodeWithTag(CREATE_RANKING_PANEL)
                .performClick()
            onNodeWithTag(RANKING_DETAILS_PANEL)
                .assertDoesNotExist()
        }
    }

    /**
     * Test case to verify that the application does not navigate to the ranking details screen
     * when the ranking ID provided is not found.
     *
     * This test simulates a scenario where the user enters an invalid ranking ID (-1 in this case)
     * and clicks the search button. It then asserts that the ranking details panel, which is expected
     * to be displayed if a ranking is found, does not exist.
     *
     * The test utilizes MockK to mock the SearchRankingViewModel and sets its rankingRequestState to
     * RequestState.Error to simulate a failed ranking request.
     *
     * **Steps:**
     * 1. Set up the SearchRankingViewModel mock and configure its rankingRequestState to RequestState.Error.
     * 2. Click on the search ranking tab.
     * 3. Enter an invalid ranking ID (-1) in the ranking ID text field.
     * 4. Click on the search ranking button.
     * 5. Assert that the ranking details panel does not exist.
     */
    @Test
    fun shouldNotNavigateToRankingDetails_whenRankingIsNotFound() {
        val mockViewModel = mockk<SearchRankingViewModel>(relaxed = true)
        every { mockViewModel.rankingRequestState } returns MutableStateFlow(RequestState.Error(""))
        with(composeTestRule) {
            onNodeWithTag(SEARCH_RANKING_TAB)
                .performClick()
            onNodeWithTag(RANKING_ID_TEXT_FIELD)
                .performTextInput("-1")
            onNodeWithTag(SEARCH_RANKING_BUTTON)
                .performClick()
            onNodeWithTag(RANKING_DETAILS_PANEL)
                .assertDoesNotExist()
        }
    }
}