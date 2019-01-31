package studio.forface.freshtv.localdata.sources

import studio.forface.freshtv.domain.entities.IChannel
import studio.forface.freshtv.localdata.TvChannelPojo
import studio.forface.freshtv.localdata.TvChannelQueries

/**
 * @author Davide Giuseppe Farella.
 * A source for Tv Channels stored locally
 *
 * Inherit from [ChannelsLocalSource]
 */
class TvChannelsLocalSource( private val queries: TvChannelQueries):
    ChannelsLocalSource<TvChannelPojo> {

    /** @return all the stored channels [TvChannelPojo] */
    override fun all(): List<TvChannelPojo> = queries.selectAll()
        .executeAsList()

    /** @return the channel [TvChannelPojo] with the given [channelId] */
    override fun channel( channelId: String ): TvChannelPojo = queries.selectById( channelId )
        .executeAsOne()

    /** @return the stored channels [TvChannelPojo] with the given [groupName] */
    override fun channelsWithGroup(groupName: String ): List<TvChannelPojo> =
        queries.selectByGroup( groupName ).executeAsList()

    /** @return the stored channels [TvChannelPojo] with the given [playlistPath] in [IChannel.playlistPaths] */
    override fun channelsWithPlaylist( playlistPath: String ): List<TvChannelPojo> =
        queries.selectByPlaylistPath( playlistPath ).executeAsList()

    /** Create a new channel [TvChannelPojo] */
    override fun createChannel( channel: TvChannelPojo) = with( channel ) {
        queries.insert(
            id =            id,
            name =          name,
            groupName =     groupName,
            imageUrl =      imageUrl,
            mediaUrls =     mediaUrls,
            playlistPaths = playlistPaths
        )
    }

    /** Delete the [TvChannelPojo] with the given [TvChannelPojo.id] */
    override fun delete( channelId: String ) {
        queries.delete( channelId )
    }

    /** Delete all the stored channels [TvChannelPojo] */
    override fun deleteAll() {
        queries.deleteAll()
    }

    /** Update an already stored channel [TvChannelPojo] */
    override fun updateChannel( channel: TvChannelPojo) {
        with( channel ) {
            queries.update( id, name, groupName, imageUrl, mediaUrls, playlistPaths )
        }
    }
}