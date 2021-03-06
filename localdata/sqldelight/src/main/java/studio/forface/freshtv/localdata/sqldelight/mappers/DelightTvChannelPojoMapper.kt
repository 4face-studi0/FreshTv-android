package studio.forface.freshtv.localdata.sqldelight.mappers

import studio.forface.freshtv.domain.entities.TvChannel
import studio.forface.freshtv.domain.entities.Url
import studio.forface.freshtv.localdata.mappers.PojoMapper
import studio.forface.freshtv.localdata.mappers.TvChannelPojoMapper
import studio.forface.freshtv.localdata.sqldelight.TvChannelPojo

/**
 * @author Davide Giuseppe Farella.
 * A Mapper for [TvChannelPojo]
 *
 * Inherit from [PojoMapper].
 */
class DelightTvChannelPojoMapper: TvChannelPojoMapper<TvChannelPojo> {

    /** @see PojoMapper.toPojo */
    override fun TvChannel.toPojo() = with(this ) {
        TvChannelPojo.Impl( id, name, groupName, imageUrl?.s, mediaUrls, playlistPaths, favorite )
    }

    /** @see PojoMapper.toEntity */
    override fun TvChannelPojo.toEntity() = with(this ) {
        TvChannel( id, name, groupName, imageUrl?.let { Url( it ) }, mediaUrls, playlistPaths, favorite )
    }
}