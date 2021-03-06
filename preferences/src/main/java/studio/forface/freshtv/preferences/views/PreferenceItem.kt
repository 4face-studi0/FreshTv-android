package studio.forface.freshtv.preferences.views

/**
 * An interface for a `Preference Item`
 *
 * @param IN is type of the values that will be received to this Item
 * @see updateValue
 *
 * @param OUT is type of the value returned by the `Action` of this Item
 * @see doOnAction
 *
 * @author Davide Giuseppe Farella
 */
internal interface PreferenceItem<in IN, out OUT> {

    /** Set an [IN] value for this [PreferenceItem] */
    fun updateValue( value: IN )

    /**
     * Execute a lambda [block] when an [OUT] action ( or value ) occurs to be delivered.
     * Value will be generated by the [PreferenceItem]; may it include and `EditText` and will deliver a [CharSequence]
     * of its `text` when it changes.
     *
     * Cannot inline
     */
    fun doOnAction( block: (OUT) -> Unit )
}

/** A [PreferenceItem] that has same type [T] as `in` and `out` */
internal interface StaticPreferenceItem<T>: PreferenceItem<T, T> {

    /**
     * @return and [T] value generated by the [PreferenceItem]; may it include and `EditText` and will return a
     * [CharSequence] of its `text` when it's requested by this function
     */
    fun getValue(): T
}