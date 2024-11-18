package com.alpaca.rankify.presentation.panel.principal.destinations.home.create_ranking.component

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.alpaca.rankify.R
import com.alpaca.rankify.util.constant.TestingTagsConstants.CreateRanking.CREATE_RANKING_BUTTON
import org.junit.Rule
import org.junit.Test

class CreateRankingButtonTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * This test verifies that the CreateRankingButton displays the "Criar Ranking" text
     * when the loading state is false.
     *
     * Given:
     * - A variable to capture the "Criar Ranking" text from resources.
     * - The loading state is set to false.
     *
     * When:
     * - The CreateRankingButton composable is rendered with the provided state.
     * - The "Criar Ranking" text is extracted from string resources.
     *
     * Then:
     * - It asserts that a node with the "Criar Ranking" text exists in the UI hierarchy.
     * This confirms that the button displays the correct text when not loading.
     */
    @Test
    fun createRankingButton_showsCreateRankingText_whenNotLoading() {
        // Given
        var createRankingText = ""
        val isLoading = false

        // When
        composeTestRule.setContent {
            CreateRankingButton(
                onCreateClick = {},
                isLoading = { isLoading }
            )
            createRankingText = stringResource(R.string.criar_ranking)
        }

        // Then
        composeTestRule
            .onNodeWithText(createRankingText)
            .assertExists()
    }

    /**
     * This test verifies that the CreateRankingButton displays a loading indicator
     * when the 'isLoading' state is true.
     *
     * Given:
     * - isLoading is set to true.
     *
     * When:
     * - CreateRankingButton is composed.
     *
     * Then:
     * - The button with the text "Criar Ranking" should not be displayed.
     *   This implies that the loading indicator is shown instead,
     *   as the button text is hidden during loading.
     */
    @Test
    fun createRankingButton_displaysLoadingIndicator_whenLoading() {
        // Given
        var createRankingText = ""
        val isLoading = true

        // When
        composeTestRule.setContent {
            CreateRankingButton(
                onCreateClick = {},
                isLoading = { isLoading }
            )
            createRankingText = stringResource(R.string.criar_ranking)
        }

        // Then
        composeTestRule
            .onNodeWithText(createRankingText)
            .assertDoesNotExist()
    }

    /**
     * This test verifies that the "Create Ranking" button is enabled when the loading state is false.
     *
     * Given:
     * - The `isLoading` state is set to `false`, indicating that no loading operation is in progress.
     *
     * When:
     * - The `CreateRankingButton` composable is rendered with the provided `isLoading` state.
     *
     * Then:
     * - It is asserted that the button with the tag `CREATE_RANKING_BUTTON` is enabled,
     *   allowing the user to interact with it.
     */
    @Test
    fun createRankingButton_isEnabled_whenNotLoading() {
        // Given
        val isLoading = false

        // When
        composeTestRule.setContent {
            CreateRankingButton(
                onCreateClick = {},
                isLoading = { isLoading }
            )
        }

        // Then
        composeTestRule
            .onNodeWithTag(CREATE_RANKING_BUTTON)
            .assertIsEnabled()
    }

    /**
     * This test verifies that the "Create Ranking" button is disabled when the loading state is true.
     *
     * Given:
     *  - The `isLoading` state is set to `true`, indicating that a loading operation is in progress.
     *
     * When:
     *  - The `CreateRankingButton` composable is rendered with the provided `isLoading` state.
     *
     * Then:
     *  - The button with the tag "CREATE_RANKING_BUTTON" should be disabled, preventing user interaction
     *    while loading is in progress.
     */
    @Test
    fun createRankingButton_isDisabled_whenLoading() {
        // Given
        val isLoading = true

        //When
        composeTestRule.setContent {
            CreateRankingButton(
                onCreateClick = {},
                isLoading = { isLoading }
            )
        }

        //Then
        composeTestRule
            .onNodeWithTag(CREATE_RANKING_BUTTON)
            .assertIsNotEnabled()
    }

    /**
     * This test verifies the behavior of the `CreateRankingButton` composable.
     *
     * It asserts that when the button is clicked and it's not in a loading state,
     * the `onCreateClick` lambda function provided to the composable is invoked.
     *
     * **Scenario:**
     * 1. **Given:**
     *    - A boolean variable `clicked` is initialized to `false` to track button click events.
     *    - A boolean variable `isLoading` is set to `false` to simulate a non-loading state.
     * 2. **When:**
     *    - The `CreateRankingButton` is composed using `setContent`.
     *    - It's configured with the `onCreateClick` lambda, which sets `clicked` to `true` when executed.
     *    - It's also configured with the `isLoading` lambda, which always returns `false`.
     *    - The button is located using its test tag (`CREATE_RANKING_BUTTON`) and clicked using `performClick`.
     * 3. **Then:**
     *    - It is asserted that the `clicked` variable is now `true`, indicating that the `onCreateClick` lambda was invoked.
     */
    @Test
    fun createRankingButton_callsOnCreateClick_whenClickedAndNotLoading() {
        // Given
        var clicked = false
        val isLoading = false

        // When
        composeTestRule.setContent {
            CreateRankingButton(
                onCreateClick = { clicked = true },
                isLoading = { isLoading }
            )
        }
        composeTestRule
            .onNodeWithTag(CREATE_RANKING_BUTTON)
            .performClick()

        // Then
        assert(clicked)
    }

    /**
     * Test case to verify that the CreateRankingButton does not trigger the onCreateClick
     * callback when the button is clicked while in a loading state.
     *
     * This test ensures that user interactions with the button are ignored while the
     * button is in a loading state, preventing unintended actions.
     *
     * **Scenario:**
     * 1. **Given:** A CreateRankingButton is configured with a callback (onCreateClick) and is set to a loading state.
     * 2. **When:** The button is clicked.
     * 3. **Then:** The onCreateClick callback should not be invoked.
     *
     * **Verification:**
     * - A boolean variable `clicked` is used to track if the callback was executed.
     * - The `isLoading` state is set to `true` to simulate the loading condition.
     * - The button is identified using its tag (CREATE_RANKING_BUTTON) and a click action is performed.
     * - Finally, an assertion is made to ensure that the `clicked` variable remains `false`, indicating that the callback was not triggered.
     */
    @Test
    fun createRankingButton_doesNotCallOnCreateClick_whenClickedAndLoading() {
        // Given
        var clicked = false
        val isLoading = true

        // When
        composeTestRule.setContent {
            CreateRankingButton(
                onCreateClick = { clicked = true },
                isLoading = { isLoading } // Configura o estado como "carregando"
            )
        }
        composeTestRule
            .onNodeWithTag(CREATE_RANKING_BUTTON)
            .performClick() // Tenta clicar no botão

        // Then
        assert(clicked.not()) // Verifica que a ação não foi chamada
    }
}