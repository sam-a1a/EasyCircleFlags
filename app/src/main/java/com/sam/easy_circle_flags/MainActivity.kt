package com.sam.easy_circle_flags

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sam.easy_circle_flags.data.Country
import com.sam.easy_circle_flags.data.isoCountries
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

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        FlagGrid(
            countries = countries,
            // Keep the scaffold's insets - the layout is edge to edge, so content
            // scrolls under the system bars - and add the grid's own margin on top.
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp
            ),
            modifier = Modifier.fillMaxSize()
        )
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
