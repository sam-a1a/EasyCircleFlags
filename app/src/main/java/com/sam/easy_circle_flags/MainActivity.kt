package com.sam.easy_circle_flags

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sam.easy_circle_flags.data.Country
import com.sam.easy_circle_flags.data.isoCountries
import com.sam.easy_circle_flags.data.searchCountries
import com.sam.easy_circle_flags.ui.CountrySearchField
import com.sam.easy_circle_flags.ui.EmptyState
import com.sam.easy_circle_flags.ui.FlagGrid
import com.sam.easy_circle_flags.ui.theme.EasyCircleFlagsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyCircleFlagsTheme {
                FlagBrowser()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlagBrowser(modifier: Modifier = Modifier) {
    // The catalogue is derived from the platform locale data, so build it once rather
    // than on every recomposition.
    val countries = remember { isoCountries() }
    var query by rememberSaveable { mutableStateOf("") }

    // derivedStateOf so that typing only re-filters, and only recomposes the grid when
    // the result actually changes - not on every keystroke that leaves it identical.
    val matches by remember(countries) {
        derivedStateOf { searchCountries(countries, query) }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            CountrySearchField(
                query = query,
                onQueryChange = { query = it },
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (matches.isEmpty()) {
                EmptyState(query = query, modifier = Modifier.fillMaxSize())
            } else {
                FlagGrid(
                    countries = matches,
                    // Keep the scaffold's bottom inset - the layout is edge to edge, so
                    // content scrolls under the navigation bar - plus the grid's margin.
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = innerPadding.calculateBottomPadding() + 16.dp
                    ),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlagGridPreview() {
    EasyCircleFlagsTheme {
        FlagGrid(
            countries = listOf(
                Country("de", "Germany"),
                Country("jp", "Japan"),
                Country("br", "Brazil")
            )
        )
    }
}
