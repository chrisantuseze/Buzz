package com.echrisantus.buzz.util

import com.echrisantus.buzz.BuzzApplication
import com.echrisantus.buzz.database.AppSharedPref
import com.echrisantus.buzz.network.model.GenreData
import org.apache.commons.lang3.StringUtils
import java.lang.Exception
import java.text.SimpleDateFormat

object Utils {

    private val genreSharedPref = AppSharedPref(BuzzApplication.applicationContext())

    fun getFormattedDate(date: String): String {
        try {
            var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val dateObj = simpleDateFormat.parse(date)
            simpleDateFormat = SimpleDateFormat("MMMM d, yyyy")
            return simpleDateFormat.format(dateObj)
        } catch (ex: Exception) {

        }
        return date
    }

    fun getGenres(genres: List<GenreData>): String {
        var aGenre = "| "
        genres?.let {
            for (genre in genres) {
                aGenre += "${genre.name} | "
            }
        }
        return aGenre
    }

    fun getGenresFromIds(genreIds: List<Int>): String {
        var genres = "| "
        for (genreId in genreIds) {
            genres += "${genreSharedPref.retrieveData(genreId.toString())} | "
        }
        return genres
    }

    fun getNoneNullValue(value: String) = if (StringUtils.isNotBlank(value)) value else StringUtils.EMPTY

}
fun main() {
    println(Utils.getFormattedDate("2020-03-05"))
}
