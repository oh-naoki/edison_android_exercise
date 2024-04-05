package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.speakbuddy.edisonandroidexercise.ui.theme.EdisonAndroidExerciseTheme

@Composable
fun FactScreen(
    viewModel: FactViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    FactContent(uiState = uiState, onUpdateFactClicked = { viewModel.updateFact() })
}

@Composable
fun FactContent(
    uiState: FactUiState,
    onUpdateFactClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp, alignment = Alignment.CenterVertically
        )
    ) {
        Text(
            text = "Fact", style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = uiState.fact, style = MaterialTheme.typography.bodyLarge
        )

        Button(onClick = onUpdateFactClicked) {
            Text(text = "Update fact")
        }
    }
}

@Preview
@Composable
private fun FactScreenPreview() {
    EdisonAndroidExerciseTheme {
        FactContent(uiState = FactUiState(fact = "Cats are cute"), onUpdateFactClicked = {})
    }
}
