package studio.forface.freshtv.androiddatabase.localdata

import androidx.paging.DataSource
import org.threeten.bp.LocalDateTime
import studio.forface.freshtv.androiddatabase.map
import studio.forface.freshtv.androiddatabase.sources.PagedMovieChannelsLocalSource
import studio.forface.freshtv.androiddatabase.sources.PagedSourceFilesLocalSource
import studio.forface.freshtv.androiddatabase.sources.PagedTvChannelsLocalSource
import studio.forface.freshtv.domain.entities.IChannel
import studio.forface.freshtv.domain.entities.MovieChannel
import studio.forface.freshtv.domain.entities.SourceFile.Epg
import studio.forface.freshtv.domain.entities.SourceFile.Playlist
import studio.forface.freshtv.domain.entities.TvChannel
import studio.forface.freshtv.localdata.mappers.MovieChannelPojoMapper
import studio.forface.freshtv.localdata.mappers.SourceFilePojoMapper
import studio.forface.freshtv.localdata.mappers.TvChannelPojoMapper
import studio.forface.freshtv.localdata.mappers.TvChannelWithGuidePojoMapper

/**
 * @author Davide Giuseppe Farella.
 * A repository for retrieve Paged [IChannel]s and EPG info locally.
 */
abstract class AbsPagedLocalData<MovieChannelPojo, SourceFilePojo, TvChannelPojo, TvChannelWithGuidePojo>(
        private val movieChannels: PagedMovieChannelsLocalSource<MovieChannelPojo>,
        private val sourceFiles: PagedSourceFilesLocalSource<SourceFilePojo>,
        private val tvChannels: PagedTvChannelsLocalSource<TvChannelPojo, TvChannelWithGuidePojo>,
        private val movieChannelMapper: MovieChannelPojoMapper<MovieChannelPojo>,
        private val sourceFilesMapper: SourceFilePojoMapper<SourceFilePojo>,
        private val tvChannelMapper: TvChannelPojoMapper<TvChannelPojo>,
        private val tvChannelWithGUideMapper: TvChannelWithGuidePojoMapper<TvChannelWithGuidePojo>
) {

    /** @return a [DataSource.Factory] of all the [Epg]s from Local Source */
    fun epgs() = sourceFiles.allEpgs().map( sourceFilesMapper ) { it.toEntity() }

    /** @return a [DataSource.Factory] of all the [MovieChannel]s from Local Source */
    fun movieChannels(): DataSource.Factory<Int, MovieChannel> = movieChannels.all()
            .map( movieChannelMapper ) { it.toEntity() }

    /**
     * @return a [DataSource.Factory] of all the [MovieChannel]s from Local Source with the given
     * [IChannel.groupName]
     * @param groupName a [String] filter for [IChannel.groupName]
     */
    fun movieChannels( groupName: String ) = movieChannels.channelsByGroup( groupName )
            .map( movieChannelMapper ) { it.toEntity() }

    /** @return a [DataSource.Factory] of all the [Playlist]s from Local Source */
    fun playlists() = sourceFiles.allPlaylists().map( sourceFilesMapper ) { it.toEntity() }

    /** @return a [DataSource.Factory] of all the [TvChannel]s from Local Source */
    fun tvChannels(): DataSource.Factory<Int, TvChannel> = tvChannels.all()
            .map( tvChannelMapper ) { it.toEntity() }

    /**
     * @return a [DataSource.Factory] of all the [TvChannel]s from Local Source with the given
     * [IChannel.groupName]
     * @param groupName a [String] filter for [IChannel.groupName]
     */
    fun tvChannels( groupName: String ) = tvChannels.channelsByGroup( groupName )
        .map( tvChannelMapper ) { it.toEntity() }

    /**
     * @return a [DataSource.Factory] of all the [TvChannelWithGuidePojo]s from Local Source with the given
     * [IChannel.groupName]
     *
     * @param groupName a [String] filter for [IChannel.groupName]
     *
     * @param time a [LocalDateTime] filter for Channel's Guide
     * Default is [LocalDateTime.now]
     */
    fun tvChannelsWithGuide( groupName: String, time: LocalDateTime = LocalDateTime.now() ) =
            tvChannels.channelsWithGuideByGroup( groupName, time ).map( tvChannelWithGUideMapper ) { it.toEntity() }
}

/** A typealias of [AbsPagedLocalData] with star-projected generics */
typealias PagedLocalData = AbsPagedLocalData<*, *, *, *>