package com.alpaca.rankify.presentation.my_rankings.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.alpaca.rankify.domain.model.Ranking
import com.alpaca.rankify.presentation.panels.principal.destinations.my_rankings.component.SavedRankingItem
import com.alpaca.rankify.util.TestingTags.SavedRankingItem.RANKING_NOT_SAVED_TEXT
import com.alpaca.rankify.util.TestingTags.SavedRankingItem.RANKING_SAVED_TEXT
import com.alpaca.rankify.util.TestingTags.SavedRankingItem.SAVED_RANKING_ITEM
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.toLocalDateTime
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for the `SavedRankingItem` composable.
 *
 * These tests verify the following aspects of the `SavedRankingItem` behavior:
 * - Correct text display based on whether the ranking has been saved on the server or not.
 * - Correct invocation of the `onClick` lambda when the item is clicked.
 * - Correct invocation of the `onDelete` lambda when the item is long-clicked.
 */
class SavedRankingItemTest {
    private val testRanking = Ranking(
        localId = 1L,
        name = "Test Ranking"
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * This test verifies that the SavedRankingItem composable displays the correct text
     * when the ranking has not been saved on the server.
     *
     * It creates a SavedRankingItem with a test ranking that is not saved.
     *
     * It then asserts that the UI element with the tag RANKING_NOT_SAVED_TEXT is displayed,
     * confirming that the "Not saved on server" message is shown.
     */
    @Test
    fun savedRankingItem_displaysCorrectText_whenNotSavedOnServer() {
        // Given a ranking that has not been saved on the server
        composeTestRule.setContent {
            SavedRankingItem(
                onClick = {},
                onDelete = {},
                ranking = testRanking
            )
        }

        // Assert that the correct text is displayed
        composeTestRule
            .onNodeWithTag(
                testTag = RANKING_NOT_SAVED_TEXT,
                useUnmergedTree = true
            )
            .assertIsDisplayed()
    }

    /**
     * This test verifies that the SavedRankingItem composable displays the correct "saved" text
     * when the ranking data indicates it has been saved on the server.
     *
     * It sets up a test Ranking object with a formatted lastUpdated date,
     * then renders the SavedRankingItem composable with this Ranking data.
     *
     * Finally, it asserts that the composable displays the "saved" text element
     * with the expected formatted update date.
     */
    @Test
    fun savedRankingItem_displaysCorrectText_whenSavedOnServer() {
        // Given a ranking that has been saved on the server with a formatted update date
        val ranking = testRanking.copy(
            lastUpdated = Clock.System.now().toLocalDateTime(timeZone = currentSystemDefault())
        )

        composeTestRule.setContent {
            SavedRankingItem(
                onClick = {},
                onDelete = {},
                ranking = ranking
            )
        }

        // Assert that the correct updated date is displayed
        composeTestRule
            .onNodeWithTag(
                testTag = RANKING_SAVED_TEXT,
                useUnmergedTree = true
            )
            .assertIsDisplayed()
    }

    /**
     * This test verifies that when a SavedRankingItem is clicked,
     * the provided onClick lambda function is invoked with the correct ranking ID.
     *
     * Given:
     * - A SavedRankingItem is displayed with a mocked onClick lambda.
     *
     * When:
     * - The user clicks on the SavedRankingItem.
     *
     * Then:
     * - The onClick lambda is called exactly once with the local ID of the displayed ranking.
     */
    @Test
    fun savedRankingItem_callsOnClick_whenClicked() {
        // Given
        val onClick: (Long) -> Unit = mockk(relaxed = true)

        composeTestRule.setContent {
            SavedRankingItem(
                onClick = onClick,
                onDelete = {},
                ranking = testRanking
            )
        }

        // When clicking on the item
        composeTestRule
            .onNodeWithTag(SAVED_RANKING_ITEM)
            .performClick()

        // Then verify that onClick was called with the correct ID
        verify(exactly = 1) {
            onClick(testRanking.localId)
        }
    }

    /**
     * Tests the behavior of the `SavedRankingItem` composable when it is long-clicked.
     *
     * **Scenario:**
     * - A `SavedRankingItem` is displayed with a mocked `onDelete` function.
     * - The item is long-clicked.
     *
     * **Expected Behavior:**
     * - The `onDelete` function is called exactly once with the `localId` of the provided `testRanking`.
     */
    @Test
    fun savedRankingItem_callsOnDelete_whenLongClicked() {
        // Given
        val onDelete: (Long) -> Unit = mockk(relaxed = true)

        composeTestRule.setContent {
            SavedRankingItem(
                onClick = {},
                onDelete = onDelete,
                ranking = testRanking
            )
        }

        // When long-clicking on the item
        composeTestRule
            .onNodeWithTag(SAVED_RANKING_ITEM)
            .performTouchInput {
                longClick()
            }

        // Then verify that onDelete was called with the correct ID
        verify(exactly = 1) {
            onDelete(testRanking.localId)
        }
    }
}
