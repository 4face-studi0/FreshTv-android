@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package studio.forface.freshtv.parsers.epg

/**
 * @author Davide Giuseppe Farella.
 * An inline class that represents the content of an EPG and expose [extractItems] for make a first
 * parsing on the content and split it in [ParsableEpgItem]s.
 */
internal inline class ParsableEpg( private val s: String ) {

    fun extractItems() : List<ParsableEpgItem> = TODO("Not implemented" )
}