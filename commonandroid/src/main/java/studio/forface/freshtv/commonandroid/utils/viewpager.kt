package studio.forface.freshtv.commonandroid.utils

import androidx.viewpager.widget.ViewPager

/*
 * Author: Davide Giuseppe Farella.
 * An utility file for Android's ViewPager's.
 */

/**
 * Add a [ViewPager.OnPageChangeListener] to the receiver [ViewPager] and trigger [callback] when
 * [ViewPager.OnPageChangeListener.onPageSelected]
 */
fun ViewPager.onPageChange( callback: (position: Int) -> Unit ) {
    addOnPageChangeListener( object : ViewPager.OnPageChangeListener {
        /**
         * This method will be invoked when the current page is scrolled, either as part
         * of a programmatically initiated smooth scroll or a user initiated touch scroll.
         *
         * @param position Position index of the first page currently being displayed.
         *                 Page position+1 will be visible if positionOffset is nonzero.
         * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
         * @param positionOffsetPixels Value in pixels indicating the offset from position.
         */
        override fun onPageScrolled( position: Int, positionOffset: Float, positionOffsetPixels: Int ) {}

        /**
         * Called when the scroll state changes. Useful for discovering when the user
         * begins dragging, when the pager is automatically settling to the current page,
         * or when it is fully stopped/idle.
         *
         * @param state The new scroll state.
         * @see ViewPager#SCROLL_STATE_IDLE
         * @see ViewPager#SCROLL_STATE_DRAGGING
         * @see ViewPager#SCROLL_STATE_SETTLING
         */
        override fun onPageScrollStateChanged( state: Int ) {}

        /**
         * This method will be invoked when a new page becomes selected. Animation is not
         * necessarily complete.
         *
         * @param position Position index of the new selected page.
         */
        override fun onPageSelected( position: Int ) {
            callback( position )
        }
    } )
}