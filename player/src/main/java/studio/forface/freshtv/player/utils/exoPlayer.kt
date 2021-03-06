package studio.forface.freshtv.player.utils

import android.content.Context
import androidx.core.net.toUri
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.BaseMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.util.Util
import studio.forface.freshtv.commonandroid.R
import studio.forface.freshtv.player.viewmodels.VideoPlayerViewModel

/** @return a [DefaultHttpDataSourceFactory] created from [exoPlayerUserAgent] */
private val Context.httpDataSourceFactory get() =
    HttpDataSourceFactory( exoPlayerUserAgent, allowCrossProtocolRedirects = true )

/** @return a [RtmpDataSourceFactory] */
private val rtmpDataSourceFactory get() = RtmpDataSourceFactory()

/**
 * @constructor for [DefaultHttpDataSourceFactory].
 * Its purpose is to offer named arguments since they're not available for Java constructor
 */
@Suppress("FunctionName")
internal fun HttpDataSourceFactory(
    userAgent: String,
    listener: TransferListener? = null,
    connectTimeoutMillis: Int = DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
    readTimeoutMillis: Int = DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
    allowCrossProtocolRedirects: Boolean = false
) = DefaultHttpDataSourceFactory(
    userAgent, listener, connectTimeoutMillis, readTimeoutMillis, allowCrossProtocolRedirects
)

/** @return a [String] user agent for `Exo Player` */
private val Context.exoPlayerUserAgent get() = Util.getUserAgent(this, getString( R.string.app_name ) )

/** @return a new instance of [MediaSourceFactory] from [VideoPlayerViewModel] */
internal val VideoPlayerViewModel.mediaSource get() = MediaSourceFactory( context )

/** A class for create [MediaSource] for `Exo Player` */
internal class MediaSourceFactory( private val context: Context ) {

    /** @return an [BaseMediaSource] from the given [url] */
    internal infix fun fromUrl( url: String ): BaseMediaSource? {
        val dataSourceFactory = when {
            url.startsWith("rtmp" ) || url.startsWith("rtsp" ) -> rtmpDataSourceFactory
            else -> context.httpDataSourceFactory
        }
        return if ( url.endsWith(".m3u8") )
            HlsMediaSource.Factory( dataSourceFactory )
                .createMediaSource( url.toUri() )
        else
            ExtractorMediaSource.Factory( dataSourceFactory )
                .createMediaSource( url.toUri() )
    }
}