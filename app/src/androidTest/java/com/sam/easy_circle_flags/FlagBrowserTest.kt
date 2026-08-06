package com.sam.easy_circle_flags

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sam.easy_circle_flags.ui.FlagGridTag
import com.sam.easy_circle_flags.ui.SearchFieldTag
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlagBrowserTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun lists_countries_on_open() {
        compose.setContent { FlagBrowser() }

        compose.onNodeWithText("Afghanistan").assertIsDisplayed()
        compose.onNodeWithText("AF").assertIsDisplayed()
    }

    @Test
    fun searching_narrows_the_grid_to_matches() {
        compose.setContent { FlagBrowser() }

        compose.onNodeWithTag(SearchFieldTag).performTextInput("germany")

        compose.onNodeWithText("Germany").assertIsDisplayed()
        compose.onNodeWithText("Afghanistan").assertDoesNotExist()
    }

    @Test
    fun a_query_matching_nothing_explains_itself() {
        compose.setContent { FlagBrowser() }

        compose.onNodeWithTag(SearchFieldTag).performTextInput("nowhereland")

        // Matched on the message rather than the query, which also appears in the field
        // the test just typed into.
        compose.onNodeWithText("No country matches", substring = true).assertIsDisplayed()
    }

    @Test
    fun clearing_the_query_brings_the_list_back() {
        compose.setContent { FlagBrowser() }

        compose.onNodeWithTag(SearchFieldTag).performTextInput("germany")
        compose.onNodeWithText("Afghanistan").assertDoesNotExist()

        // Through the clear button, which is how a user actually empties the field.
        compose.onNodeWithContentDescription("Clear search").performClick()

        // Scrolled to rather than asserted in place: clearing brings the showcase header
        // back, and with the keyboard still up that pushes the first row out of the
        // viewport, where a lazy grid has not composed it yet.
        compose.onNodeWithTag(FlagGridTag).performScrollToNode(hasText("Afghanistan"))
        compose.onNodeWithText("Afghanistan").assertIsDisplayed()
    }
}
