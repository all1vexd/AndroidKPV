import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.itis.hw5.R
import ru.itis.hw5.constants.StatusConstant
import ru.itis.hw5.constants.StringResources
import ru.itis.hw5.data.database.entities.Movie
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MovieItem(
    movie: Movie,
    onDelete: () -> Unit,
    onFavoriteClick: () -> Unit,
    onMovieClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onMovieClick()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = movie.director ?: stringResource(R.string.unknown_director),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Row {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (movie.isFavorite) Color.Red else Color.Gray
                        )
                    }
                    IconButton(
                        onClick = onDelete
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_action)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(18.dp))
                    Text(movie.rating?.toString() ?: stringResource(R.string.default_rating), modifier = Modifier.padding(start = 4.dp))
                }

                val (statusText, statusColor) = getStatusInfo(movie.status ?: StatusConstant.STATUS_PLANNED)

                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, statusColor)
                ) {
                    Text(
                        text = statusText,
                        color = statusColor,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.added_date) + formatTimestamp(movie.addedDate),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat(StringResources.DATE_FORMAT_PATTERN, Locale.getDefault())
    return format.format(date)
}

@Composable
fun getStatusInfo(status: String): Pair<String, Color> {
    return when (status) {
        StatusConstant.STATUS_WATCHED -> stringResource(R.string.status_watched) to Color(0xFF4CAF50)
        StatusConstant.STATUS_WATCHING -> stringResource(R.string.status_watching) to Color(0xFF2196F3)
        StatusConstant.STATUS_PLANNED -> stringResource(R.string.status_planned) to Color(0xFFFF9800)
        StatusConstant.STATUS_DROPPED -> stringResource(R.string.status_dropped) to Color(0xFFF44336)
        else -> stringResource(R.string.status_unknown) to Color.Gray
    }
}