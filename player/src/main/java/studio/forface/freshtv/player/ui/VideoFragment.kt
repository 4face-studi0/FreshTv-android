package studio.forface.freshtv.player.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.postDelayed
import kotlinx.android.synthetic.main.fragment_player_video.*
import org.koin.androidx.viewmodel.ext.viewModel
import org.koin.core.parameter.parametersOf
import studio.forface.freshtv.commonandroid.frameworkcomponents.NestedFragment
import studio.forface.freshtv.player.R
import studio.forface.freshtv.player.uiModels.ChannelSourceUiModel
import studio.forface.freshtv.player.viewmodels.ChannelSourceViewModel
import studio.forface.freshtv.player.viewmodels.VideoPlayerViewModel


/**
 * A [NestedFragment] for the player of [PlayerFragment]
 *
 * @author Davide Giuseppe Farella
 */
internal class VideoFragment : NestedFragment<PlayerFragment>( R.layout.fragment_player_video ) {

    /** @return [String] Channel id from [rootFragment] */
    private val channelId by lazy { rootFragment.channelId }

    /** A reference to [VideoPlayerViewModel] */
    private val playerViewModel by viewModel<VideoPlayerViewModel>()

    /** A reference to [ChannelSourceViewModel] */
    private val sourceViewModel by viewModel<ChannelSourceViewModel> { parametersOf( channelId ) }

    /** When the `Activity` is created for [VideoFragment] */
    override fun onActivityCreated( savedInstanceState: Bundle? ) {
        super.onActivityCreated( savedInstanceState )

        sourceViewModel.source.observe {
            doOnData( ::onSourceReady )
            doOnError { notifier.error( it ) }
        }

        playerViewModel.errors.observe {
            doOnError { notifier.error( it ) }
        }

        playerViewModel.playbackState.observeData {
            when ( it ) {
                is VideoPlayerViewModel.PlaybackState.Success -> onUrlLoadingSuccess( it.url )
                is VideoPlayerViewModel.PlaybackState.Error -> onUrlLoadingFailed( it.url )
            }
        }
    }

    /** When the [View] is created for [PlayerFragment] */
    override fun onViewCreated( view: View, savedInstanceState: Bundle? ) {
        super.onViewCreated( view, savedInstanceState )
        playerViewModel.setPlayerView( playerView )
    }

    /** When a [ChannelSourceUiModel] is received from [ChannelSourceViewModel] */
    private fun onSourceReady( source: ChannelSourceUiModel ) {
        playerViewModel.currentUrl = source.url

        videoPlayerUrlTextView.apply {
            visibility = View.VISIBLE
            text = source.url
            postDelayed(3_000 ) { visibility = View.GONE }
        }
    }

    /** When the player failed to load the given [url] */
    private fun onUrlLoadingFailed( url: String ) {
        sourceViewModel.notifyFailure( url )
    }

    /** When the player succeed to load the given [url] */
    private fun onUrlLoadingSuccess( url: String ) {
        sourceViewModel.notifySuccess( url )
    }
}