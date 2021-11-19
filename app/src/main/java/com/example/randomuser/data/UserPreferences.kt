import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences>

    init {
        dataStore = applicationContext.createDataStore(
            name = "app_preferences"
        )
    }

    val bookmark: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_BOOKMARK]
        }

    /*val deletedUsers: Flow<List<String>?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_DELETED_USERS]
        }*/

    suspend fun saveBookmark(bookmark: String) {
        dataStore.edit { preferences ->
            preferences[KEY_BOOKMARK] = bookmark
        }
    }

    /*suspend fun saveDeletedUsers(deletedUsers: ArrayList<String>) {
        dataStore.edit { preferences ->
            preferences[KEY_DELETED_USERS] = deletedUsers
        }
    }*/

    companion object {
        val KEY_BOOKMARK = preferencesKey<String>("key_bookmark")
        //val KEY_DELETED_USERS = preferencesKey<List<String>>("key_deleted_users")
    }
}