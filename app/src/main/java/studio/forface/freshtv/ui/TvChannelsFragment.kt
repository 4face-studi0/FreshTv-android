package studio.forface.freshtv.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.viewModel
import org.koin.core.parameter.parametersOf
import studio.forface.freshtv.R
import studio.forface.freshtv.commonandroid.frameworkcomponents.NestedFragment
import studio.forface.freshtv.ui.TvChannelsFragmentDirections.Companion.actionTvChannelsFragmentToPlayerFragment
import studio.forface.freshtv.ui.adapters.TvChannelsAdapter
import studio.forface.freshtv.viewmodels.TvChannelsViewModel

/**
 * @author Davide Giuseppe Farella
 * A `Fragment` for see the stored `TvChannels` with their `TvGuide`
 *
 * Inherit from [NestedFragment]
 */
internal class TvChannelsFragment: NestedFragment( R.layout.fragment_recycler_view ) {

    companion object {
        /** @return a new instance of [TvChannelsFragment] with [groupName] in [TvChannelsFragment.mArguments] */
        operator fun invoke( groupName: String ) = TvChannelsFragment().apply {
            val args = bundleOf(ARG_GROUP_NAME to groupName )
            if ( arguments != null ) requireArguments().putAll( args )
            else arguments = args
        }
        /** A key for [groupName] argument */
        private const val ARG_GROUP_NAME = "extra.group-name"
    }

    /** A reference to [TvChannelsAdapter] for `recyclerView` */
    private val adapter = TvChannelsAdapter().apply {
        onItemClick = { navController.navigate( actionTvChannelsFragmentToPlayerFragment( it.id ) ) }
        onItemFavoriteChange = { channelsViewModel.setFavoriteChannel( it ) }
    }

    /** A reference to [TvChannelsViewModel] for retrieve the stored `Channel`s */
    private val channelsViewModel by viewModel<TvChannelsViewModel> { parametersOf( groupName ) }

    /** An OPTIONAL [String] received from [getArguments] for filter elements by their `groupName` */
    private val groupName by lazy { requireArguments().getString( ARG_GROUP_NAME )!! }

    /** When the `Activity` is created */
    override fun onActivityCreated( savedInstanceState: Bundle? ) {
        super.onActivityCreated( savedInstanceState )
        channelsViewModel.channels.observe {
            doOnData { adapter.submitList( it ) }
            doOnError { notifier.error( it ) }
            doOnLoadingChange( ::onLoading )
        }
    }

    /** When the [TvChannelsFragment]s [View] is created */
    override fun onViewCreated( view: View, savedInstanceState: Bundle? ) {
        super.onViewCreated( view, savedInstanceState )
        val recyclerView = view.findViewById<RecyclerView>( R.id.recyclerView )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager( context )
    }

    /** Update the loading state */
    private fun onLoading( loading: Boolean ) {
        val progressBar = requireView().findViewById<ContentLoadingProgressBar>( R.id.progressBar )
        if ( loading ) progressBar.show() else progressBar.hide()
    }
}