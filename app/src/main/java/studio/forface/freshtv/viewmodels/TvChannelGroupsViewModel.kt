package studio.forface.freshtv.viewmodels

import androidx.lifecycle.ViewModel
import studio.forface.freshtv.commonandroid.frameworkcomponents.ScopedViewModel
import studio.forface.freshtv.presenters.ChannelGroupsPresenter
import studio.forface.freshtv.uimodels.ChannelGroupUiModel
import studio.forface.viewstatestore.LockedViewStateStore
import studio.forface.viewstatestore.ViewStateStore

/**
 * @author Davide Giuseppe Farella
 * A [ViewModel] that get `TvChannel`s Groups
 *
 * Inherit from [ScopedViewModel]
 */
internal class TvChannelGroupsViewModel(
        private val presenter: ChannelGroupsPresenter
): ScopedViewModel() {

    /** A [LockedViewStateStore] of [List] of [ChannelGroupUiModel] */
    val groups = ViewStateStore<List<ChannelGroupUiModel>>().lock

    init {
        groups.setLoading()
        runCatching { presenter.tv() }
                .onSuccess { groups.setData( it ) }
                .onFailure { groups.setError( it ) }
    }
}