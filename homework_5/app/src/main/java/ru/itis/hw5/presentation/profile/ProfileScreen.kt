import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.itis.hw5.R
import ru.itis.hw5.data.database.AppDatabase
import ru.itis.hw5.data.repository.MovieRepository
import ru.itis.hw5.data.repository.UserRepository
import ru.itis.hw5.presentation.profile.ProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: Int,
    onLogout: () -> Unit,
    onNavigateBack: () -> Unit
) {

    val context = LocalContext.current
    val viewModel = remember(userId) {
        val database = AppDatabase.getInstance(context)
        val repository = UserRepository(database, context)
        ProfileViewModel(repository, userId)
    }

    val repository = remember { MovieRepository(AppDatabase.getInstance(context)) }

    var movieCount by remember { mutableIntStateOf(0) }
    var avgRating by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(userId) {
        movieCount = repository.getMovieCount(userId)
        avgRating = repository.getAverageRating(userId) ?: 0f
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile_title)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(20.dp).fillMaxSize(),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = viewModel.username,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    label = stringResource(R.string.movies_count_label),
                    value = movieCount.toString(),
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
                StatCard(
                    label = stringResource(R.string.average_rating_label),
                    value = String.format("%.1f", avgRating),
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            }

            Spacer(modifier = Modifier.weight(1f))


            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.logout_button))
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.deleteAccount(onComplete = onLogout)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.delete_account_button)
                )
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier, containerColor: androidx.compose.ui.graphics.Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
            Text(text = label, style = MaterialTheme.typography.labelMedium)
        }
    }
}