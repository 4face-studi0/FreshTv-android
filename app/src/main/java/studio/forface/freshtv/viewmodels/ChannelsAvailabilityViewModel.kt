package studio.forface.freshtv.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import studio.forface.freshtv.commonandroid.frameworkcomponents.ScopedViewModel
import studio.forface.freshtv.presenters.ChannelsAvailabilityPresenter
import studio.forface.freshtv.presenters.invoke
import studio.forface.freshtv.uimodels.ChannelsAvailabilityUiModel
import studio.forface.viewstatestore.ViewStateStore
import studio.forface.viewstatestore.postData
import studio.forface.viewstatestore.postError
import studio.forface.viewstatestore.postLoading

/**
 * @author Davide Giuseppe Farella
 * A [ViewModel] that checks the availability of Channels
 *
 * Inherit from [ScopedViewModel]
 */
internal class ChannelsAvailabilityViewModel(
    private val presenter: ChannelsAvailabilityPresenter
): ScopedViewModel() {

    /** A [ViewStateStore] of [ChannelsAvailabilityUiModel] */
    val channelsAvailability = ViewStateStore<ChannelsAvailabilityUiModel>()

    init {
        // When ViewModel is instantiated, start observing for UiModels
        launch( IO ) { startObserving() }
    }

    private suspend fun startObserving() {
        channelsAvailability.postLoading()

        runCatching {

            // Receive Models
            for ( uiModel in presenter { observe() } )
                channelsAvailability.postData( uiModel )

        // If some error occurs, notify it, wait and then try again
        }.onFailure {
            channelsAvailability.postError( it )
            delay( DEFAULT_ERROR_DELAY )
            startObserving()
        }
    }
}