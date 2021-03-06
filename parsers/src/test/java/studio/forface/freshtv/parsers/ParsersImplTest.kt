package studio.forface.freshtv.parsers

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import studio.forface.freshtv.domain.entities.SourceFile
import studio.forface.freshtv.domain.entities.SourceFile.Epg
import studio.forface.freshtv.domain.entities.SourceFile.Playlist
import studio.forface.freshtv.domain.entities.TvGuide

/**
 * @author Davide Giuseppe Farella
 * Test class for [ParsersImpl]
 */
internal class ParsersImplTest {

    private val mockEpgStream = mockEpgContent.byteInputStream()
    private val mockPlaylistStream = mockPlaylistContent.byteInputStream()

    private val mockLocal = mockk<FileContentResolver.Local> {
        coEvery { this@mockk( any() ) } answers { mockEpgStream }
    }

    private val mockRemote = mockk<FileContentResolver.Remote> {
        coEvery { this@mockk( any() ) } answers { mockPlaylistStream }
    }

    private val mockResolver = spyk( FileContentResolver( mockLocal, mockRemote ) ) {

        coEvery { this@spyk( any<Playlist>() ) } coAnswers
                { this@spyk( firstArg<Playlist>().type, mockPlaylistContent ) }

        coEvery { this@spyk( any<Epg>() ) } coAnswers
                { this@spyk( firstArg<Epg>().type, mockEpgContent ) }
    }

    private val parsers = ParsersImpl( mockResolver )

    @Test
    fun `readFrom epg executeCorrectly`() {
        var guideCalled = false
        var progressCalled = false

        runBlocking {
            parsers.readFrom(
                Epg("", SourceFile.Type.LOCAL ),
                { guideCalled = true },
                { /* Error */ },
                { progressCalled = true }
            )
        }

        coVerify( exactly = 1 ) { mockLocal.invoke( any() ) }
        coVerify( exactly = 0 ) { mockRemote.invoke( any() ) }
        assertTrue( guideCalled )
        assertTrue( progressCalled )
    }

    @Test
    fun `readFrom playlist executeCorrectly`() {
        var channelCalled = false
        var groupCalled = false
        var errorCalled = false

        runBlocking {
            parsers.readFrom(
                Playlist("", SourceFile.Type.REMOTE ),
                { channelCalled = true },
                { groupCalled = true },
                { errorCalled = true }
            )
        }

        coVerify( exactly = 0 ) { mockLocal.invoke( any() ) }
        coVerify( exactly = 1 ) { mockRemote.invoke( any() ) }
        assertTrue( channelCalled )
        assertTrue( groupCalled )
        assertTrue( errorCalled )
    }

    @Test
    fun `readFrom remote fromRemoteSource`() {
        runBlocking {
            parsers.readFrom(
                Playlist("", SourceFile.Type.REMOTE ),
                {}, {}, {}
            )
        }

        coVerify( exactly = 0 ) { mockLocal.invoke( any() ) }
        coVerify( exactly = 1 ) { mockRemote.invoke( any() ) }
    }

    @Test // test only manually due to http call
    fun `epg realTest`() {
        val source = ParsersImpl( FileContentResolver( mockLocal, FileContentResolver.Remote() ) )

        runBlocking {
            source.readFrom(
                realEpgSource,
                onTvGuide = { println( it ) },
                onError = { println( "${it.reason.name} - ${it.rawChannel}" ) },
                onProgress = { println( "progress: $it" ) }
            )
        }
    }

    @Test // test only manually due to http call
    fun `epg dates realTest`() {
        val source = ParsersImpl( FileContentResolver( mockLocal, FileContentResolver.Remote() ) )

        val guides = mutableListOf<TvGuide>()

        runBlocking {
            source.readFrom(
                Epg("http://iptv-epg.com/c77e-hnfzcd.xml", SourceFile.Type.REMOTE),
                onTvGuide = { guides += it }
            )
        }

        val count = guides.size
        val result = guides.sortedByDescending { it.startTime }
            .take(20 )
            .joinToString( separator = "\n" ) { "${it.startTime} - ${it.endTime} " }

        println( "count: $count" )
        println( result )
    }

    @Test // test only manually due to http call
    fun `playlist realTest`() {
        val source = ParsersImpl( FileContentResolver( mockLocal, FileContentResolver.Remote() ) )

        runBlocking {
            source.readFrom(
                    Playlist("https://sourcetv.info/dl/01/it29.m3u", SourceFile.Type.REMOTE ),
                    onChannel = { println( it ) },
                    onGroup = { println( it ) },
                    onError = { println( "${it.reason.name} - ${it.rawChannel}" ) }
            )
        }
    }
}