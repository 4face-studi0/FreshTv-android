package studio.forface.freshtv.commonandroid.notifier

import android.app.Activity
import android.content.res.Resources
import android.util.Log
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import studio.forface.freshtv.domain.gateways.Notifier
import studio.forface.freshtv.domain.gateways.Notifier.Action
import studio.forface.freshtv.domain.gateways.OptionalAction
import studio.forface.freshtv.domain.utils.nonNullMessage
import studio.forface.viewstatestore.ViewState
import timber.log.Timber

/**
 * @author Davide Giuseppe Farella.
 * Android implementation of [Notifier].
 */
class AndroidNotifier internal constructor(
    private val resources: Resources,
    private val toast: Toast
): Notifier<AndroidNotifier.ActionBuilder> {

    /**
     * A reference to [SnackbarManager] for show a [Snackbar] if app is in foreground
     * The value will be set on [Activity.onStart] and removed ( null ) on [Activity.onStop]
     */
    private var snackbarManager: SnackbarManager? = null

    /** Set [AndroidNotifier.snackbarManager] to the given [SnackbarManager] */
    internal fun setSnackbarManager( snackbarManager: SnackbarManager ) {
        this.snackbarManager = snackbarManager
    }

    /**
     * If [AndroidNotifier.snackbarManager] is same as the given [SnackbarManager] set it to `null`
     * This is for avoid that an `Activity` remove the [AndroidNotifier.snackbarManager] that has been set from
     * another `Activity`
     */
    internal fun removeSnackbarManager( snackbarManager: SnackbarManager ) {
        if ( this.snackbarManager === snackbarManager )
            this.snackbarManager = null
    }


    /* = = = = = = = = ERROR = = = = = = = = */

    /** Show an error event though a [ViewState.Error] */
    fun error( error: ViewState.Error, optionalAction: AndroidOptionalAction = null ) {
        this( Type.Error, error, optionalAction )
    }

    /** Show an error event though a [Throwable] and an OPTIONAL [String] message */
    override fun error(
        throwable: Throwable,
        message: CharSequence, // = throwable.nonNullMessage
        optionalAction: AndroidOptionalAction // = null
    ) {
        this( Type.Error, throwable, message, optionalAction )
    }

    /** Show an error event though a [Throwable] and a [StringRes] */
    fun error( throwable: Throwable, @StringRes messageRes: Int?, optionalAction: AndroidOptionalAction = null ) {
        this( Type.Error, throwable, messageRes, optionalAction )
    }

    /** Show an error event though a [CharSequence] */
    override fun error( message: CharSequence, optionalAction: AndroidOptionalAction /* = null */ ) {
        this( Type.Error, message, optionalAction )
    }

    /* = = = = = = = = INFO = = = = = = = = */

    /** Show an info event though a [ViewState.Error] */
    fun info( error: ViewState.Error, optionalAction: AndroidOptionalAction = null ) {
        this( Type.Info, error, optionalAction )
    }

    /** Show an info event though a [Throwable] and an OPTIONAL [String] message */
    override fun info(
        throwable: Throwable,
        message: CharSequence, // = throwable.nonNullMessage
        optionalAction: AndroidOptionalAction // = null
    ) {
        this( Type.Info, throwable, message, optionalAction )
    }

    /** Show an info event though a [Throwable] and a [StringRes] */
    fun info( throwable: Throwable, @StringRes messageRes: Int?, optionalAction: AndroidOptionalAction = null ) {
        this( Type.Info, throwable, messageRes, optionalAction )
    }

    /** Show an info event though a [CharSequence] */
    override fun info( message: CharSequence, optionalAction: AndroidOptionalAction /* = null */ ) {
        this( Type.Info, message, optionalAction )
    }

    /* = = = = = = = = WARN = = = = = = = = */

    /** Show a warn event though a [ViewState.Error] */
    fun warn( error: ViewState.Error, optionalAction: AndroidOptionalAction = null ) {
        this( Type.Warn, error, optionalAction )
    }

    /** Show a warn event though a [Throwable] and an OPTIONAL [String] message */
    override fun warn(
        throwable: Throwable,
        message: CharSequence, // = throwable.nonNullMessage
        optionalAction: AndroidOptionalAction // = null
    ) {
        this( Type.Warn, throwable, message, optionalAction )
    }

    /** Show a warn event though a [Throwable] and a [StringRes] */
    fun warn( throwable: Throwable, @StringRes messageRes: Int?, optionalAction: AndroidOptionalAction = null ) {
        this( Type.Warn, throwable, messageRes, optionalAction )
    }

    /** Show a warn event though a [CharSequence] */
    override fun warn( message: CharSequence, optionalAction: AndroidOptionalAction /* = null */ ) {
        this( Type.Warn, message, optionalAction )
    }


    /* = = = = = = = = PRIVATE METHODS = = = = = = = = */

    /** Handle an event of the given [Type] though [Timber] and [SnackbarManager] or [Toast] */
    private operator fun invoke( type: Type, error: ViewState.Error, optionalAction: AndroidOptionalAction ) {
        this( type, error.throwable, error.getMessage( resources ), optionalAction )
    }

    /** Handle an event of the given [Type] though [Timber] and [SnackbarManager] or [Toast] */
    private operator fun invoke(
        type: Type,
        throwable: Throwable,
        message: CharSequence = throwable.nonNullMessage,
        optionalAction: AndroidOptionalAction
    ) {
        Timber.log( type.logLevel, throwable, message.toString() )
        publish( type, message, optionalAction )
    }

    /** Handle an event of the given [Type] though [Timber] and [SnackbarManager] or [Toast] */
    private operator fun invoke(
        type: Type,
        throwable: Throwable,
        @StringRes messageRes: Int?,
        optionalAction: AndroidOptionalAction
    ) {
        val message = messageRes?.let { resources.getText( it ) } ?: throwable.nonNullMessage
        this( type, throwable, message.toString(), optionalAction )
    }

    /** Handle an event of the given [Type] though [Timber] and [SnackbarManager] or [Toast] */
    private operator fun invoke(
            type: Type,
            message: CharSequence,
            optionalAction: AndroidOptionalAction
    ) {
        Timber.log( type.logLevel, message.toString() )
        publish( type, message, optionalAction )
    }

    /** Create a [Snackbar] if possible, else a [Toast] */
    private fun publish(
            type: Type,
            message: CharSequence,
            optionalAction: AndroidOptionalAction
    ) {
        val action = optionalAction?.let {
            val builder = ActionBuilder( resources )
            it( builder )
            builder.build()
        }

        snackbarManager?.showSnackbar( type.snackbarType, message, action )
            ?: toast.show( type.toastType, message )
    }


    /** An enum class for the types of events to log */
    private enum class Type(
        internal val logLevel: Int,
        internal val snackbarType: SnackbarType,
        internal val toastType: Toast.Type
    ) {
        Error ( Log.ERROR, SnackbarType.Error, Toast.Type.Error ),
        Info ( Log.INFO, SnackbarType.Info, Toast.Type.Info ),
        Warn ( Log.WARN, SnackbarType.Warn, Toast.Type.Warn )
    }


    /**
     * A builder for create [Notifier.Action] within a DSL style
     * Inherit from [Notifier.ActionBuilder] and add the possibility to set the [Notifier.Action.name] from a [StringRes]
     */
    class ActionBuilder( private val resources: Resources ): Notifier.ActionBuilder() {
        /** @see Action.name */
        @StringRes var actionNameRes: Int? = null

        /**
         * @return [Notifier.Action] created from [actionName] ( or [actionNameRes] ) and [actionBlock]
         * @throws KotlinNullPointerException if both [actionNameRes] and [actionName] have not been set.
         */
        override fun build(): Notifier.Action {
            val aName = actionNameRes?.let { resources.getText( it ) } ?: actionName!!
            return Notifier.Action( aName, actionBlock )
        }
    }
}

/** A typealias for [OptionalAction] with [AndroidNotifier.ActionBuilder] */
typealias AndroidOptionalAction = OptionalAction<AndroidNotifier.ActionBuilder>