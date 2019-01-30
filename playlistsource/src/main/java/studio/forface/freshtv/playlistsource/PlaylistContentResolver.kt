package studio.forface.freshtv.playlistsource

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import studio.forface.freshtv.domain.entities.Playlist
import studio.forface.freshtv.playlistsource.PlaylistContentResolver.Source
import java.io.File

/**
 * @author Davide Giuseppe Farella
 * A class for retrieve the content of a given [Playlist] from the appropriate [Source]
 */
internal class PlaylistContentResolver(
        private val local: PlaylistContentResolver.Local = Local,
        private val remote: PlaylistContentResolver.Remote = Remote( HttpClient() )
) {

    /** @return the [String] content of the given [Playlist] */
    suspend operator fun invoke( playlist: Playlist ): String {
        val source = when( playlist.type ) {
            Playlist.Type.LOCAL -> local
            Playlist.Type.REMOTE -> remote
        }
        return source( playlist.path )
    }


    /** An interface for retrieve the content of a given [String] path */
    interface Source {

        /** @return the [String] content of the given [path] */
        suspend operator fun invoke( path: String ): String
    }


    /** A [Source] for retrieve the content of a Local file. */
    object Local : Source {
        override suspend fun invoke( path: String ) = File( path ).readText()
    }

    /** A [Source] for retrieve the content of a Remote file. */
    class Remote( private val client: HttpClient ): Source {
        override suspend fun invoke( path: String ) = client.get<String>( path )
    }
}