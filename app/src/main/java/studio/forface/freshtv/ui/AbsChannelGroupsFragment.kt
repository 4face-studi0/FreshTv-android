package studio.forface.freshtv.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.postDelayed
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_view_pager_tabbed.*
import studio.forface.freshtv.R
import studio.forface.freshtv.commonandroid.ui.NestedFragment
import studio.forface.freshtv.commonandroid.ui.ParentFragment
import studio.forface.freshtv.commonandroid.utils.onPageChange
import studio.forface.freshtv.ui.AbsChannelGroupsFragment.GroupsAdapter
import studio.forface.freshtv.uimodels.ChannelGroupUiModel
import studio.forface.freshtv.uimodels.ChannelGroupsUiModel
import studio.forface.freshtv.viewmodels.ChannelGroupsViewModel
import studio.forface.materialbottombar.dsl.panel
import studio.forface.materialbottombar.panels.AbsMaterialPanel
import studio.forface.materialbottombar.panels.items.PrimaryPanelItem
import studio.forface.materialbottombar.panels.params.titleColorRes
import studio.forface.materialbottombar.panels.params.titleTextRes

/**
 * An abstract `Fragment` for see the stored `Channel`s Groups
 * Inherit from [ParentFragment]
 *
 * @param createChildFragment a lambda that creates the required [ChannelsFragment] for the [GroupsAdapter.getItem]
 *
 *
 * @author Davide Giuseppe Farella
 */
internal abstract class AbsChannelGroupsFragment(
    createChildFragment: (groupName: String) -> ChannelsFragment<*>
) : ParentFragment( R.layout.fragment_view_pager_tabbed ) {

    /** A reference to [GroupsAdapter] for [viewPager] */
    private val adapter by lazy { GroupsAdapter( requireFragmentManager(), createChildFragment ) }

    /** A reference to [ChannelGroupsViewModel] for retrieve the stored `Channel`s */
    protected abstract val channelGroupsViewModel: ChannelGroupsViewModel

    /** A `MaterialPanel` for show the list of available `Groups` */
    private val groupsPanel by lazy {
        panel( wrapToContent = false ) {
            header {
                titleTextRes = R.string.title_channels_groups
                titleColorRes = R.color.colorPrimary
            }
            body {}
        }
    }

    /** An [AbsMaterialPanel.Body] for [groupsPanel] */
    private var groupsPanelBody = AbsMaterialPanel.Body()
        set( value ) {
            field = value
            groupsPanel.body = value
        }

    /** An id for [groupsPanel] */
    protected abstract val groupPanelId: Int

    /** @see ParentFragment.isRootFragment */
    override val isRootFragment = true

    /** @see ParentFragment.menuRes */
    override val menuRes: Int? get() = R.menu.menu_channels_groups

    /** Reference to [TabLayout] */
    private val tabLayout get() = requireView().findViewById<TabLayout>( R.id.tabLayout )

    /** Reference to [ViewPager] */
    private val viewPager get() = requireView().findViewById<ViewPager>( R.id.viewPager )

    /** When the `Activity` is created */
    override fun onActivityCreated( savedInstanceState: Bundle? ) {
        super.onActivityCreated( savedInstanceState )
        channelGroupsViewModel.groups.observe {
            doOnData( ::onGroups )
            doOnError { notifier.error( it ) }
        }
    }

    /** When the [AbsChannelGroupsFragment]s [View] is created */
    override fun onViewCreated( view: View, savedInstanceState: Bundle? ) {
        super.onViewCreated( view, savedInstanceState )
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager( viewPager )
        viewPager.onPageChange( ::onViewPagerPageChange )
    }

    /**
     * When a [MenuItem] is selected from Options Menu.
     * @see menuRes
     */
    override fun onOptionsItemSelected( item: MenuItem ): Boolean {
        when ( item.itemId ) {
            R.id.action_channels_groups -> showPanel( groupPanelId, groupsPanel )
        }
        return super.onOptionsItemSelected( item )
    }

    /** When the `Fragment` is destroyed */
    override fun onDestroy() {
        dismissAndRemovePanel( groupPanelId )
        super.onDestroy()
    }

    /** When [ChannelGroupsUiModel]s are received */
    private fun onGroups( groupsUiModel: ChannelGroupsUiModel ) {
        val ( groups, savedPosition ) = groupsUiModel

        // Set the tabMode to tabLayout
        tabLayout.tabMode = if ( groups.size >= 4 ) TabLayout.MODE_SCROLLABLE else TabLayout.MODE_FIXED
        // Update the adapter for viewPager
        adapter.groups = groups
        // Update the currentItem of ViewPager if savedPosition is not null.
        // The action will be executed with a delay of 100 ms for let the viewPager and relative tabLayout asset first
        if ( savedPosition != null )
            viewPager.postDelayed(100 ) { viewPager.currentItem = savedPosition }

        // Update the panel
        updatePanelGroups( groups )
    }

    /** When the current page is changed for [viewPager] */
    private fun onViewPagerPageChange( position: Int ) {
        groupsPanelBody.setSelected( position )
        val currentGroupName = adapter.groups[position].name
        channelGroupsViewModel.saveLastGroupName( currentGroupName )
    }

    /** Update the groups for [groupsPanel] */
    private fun updatePanelGroups( groups: List<ChannelGroupUiModel> ) {
        groupsPanelBody = AbsMaterialPanel.Body(
            groups.mapIndexed { index, group ->
                PrimaryPanelItem()
                    .id( index )
                    .titleText( group.name )
            }
        )   .setSelected( viewPager.currentItem )
            .itemClickListener { index, _ -> viewPager.currentItem = index }
    }

    /**
     * A [FragmentStatePagerAdapter] for `Channel`s Groups
     * @param createChildFragment a lambda that creates the required [NestedFragment] for the [getItem]
     */
    private class GroupsAdapter(
        fragmentManager: FragmentManager,
        private val createChildFragment: (groupName: String) -> NestedFragment<*>
    ): FragmentStatePagerAdapter( fragmentManager ) {

        /** A [List] of available [ChannelGroupUiModel] */
        var groups: List<ChannelGroupUiModel> = emptyList()
            set( value ) {
                field = value
                notifyDataSetChanged()
            }

        /** @return size of [groups] */
        override fun getCount() = groups.size

        /**
         * @return a new instance of [NestedFragment], with [ChannelGroupUiModel.name] of item at the given
         * [position]
         */
        override fun getItem( position: Int ) = createChildFragment( groups[position].name )

        /** @return [ChannelGroupUiModel.name] for item at the given [position] */
        override fun getPageTitle( position: Int ) = groups[position].name
    }
}