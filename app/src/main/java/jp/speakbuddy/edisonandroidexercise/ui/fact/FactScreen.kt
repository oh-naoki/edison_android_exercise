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
import jp.speakbuddy.edisonandroidexercise.network.FactResponse
import jp.speakbuddy.edisonandroidexercise.ui.theme.EdisonAndroidExerciseTheme

@Composable
fun FactScreen(
    viewModel: FactViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
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
        Title(containsCats = uiState.containsCats)
        Body(fact = uiState.fact, isLongFact = uiState.isLongFact)
        Button(onClick = onUpdateFactClicked) {
            Text(text = "Update fact")
        }
    }
}

@Composable
fun Title(
    containsCats: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            text = "Fact", style = MaterialTheme.typography.titleLarge
        )
        if (containsCats) {
            Text(text = "Multiple cats!!", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun Body(
    fact: FactResponse,
    isLongFact: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = fact.fact, style = MaterialTheme.typography.bodyLarge
        )

        if (isLongFact) {
            Text(
                text = "length: ${fact.length}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.End)
            )
        }
    }
}

@Preview
@Composable
private fun FactScreenPreview() {
    val label = "Cat is cute"
    EdisonAndroidExerciseTheme {
        FactContent(uiState = FactUiState(fact = FactResponse(label, label.length)), onUpdateFactClicked = {})
    }
}
