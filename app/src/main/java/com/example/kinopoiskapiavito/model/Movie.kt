package com.example.kinopoiskapiavito.model




data class Movie(
    val id: Int,
    val externalId: ExternalId? = null,
    val name: String? = null,
    val alternativeName: String? = null,
    val enName: String? = null,
    val typeNumber: Int? = null,
    val year: Int? = null,
    val description: String? = null,
    val shortDescription: String? = null,
    val slogan: String? = null,
    val status: String? = null,
    val facts: List<Fact>? = null,
    val rating: Rating? = null,
    val votes: Votes? = null,
    val movieLength: Int? = null,
    val ratingMpaa: String? = null,
    val ageRating: Int? = null,
    val logo: Logo? = null,
    val poster: ShortImage? = null,
    val backdrop: ShortImage? = null,
    val genres: List<ItemName>? = null,
    val countries: List<ItemName>? = null,
    val persons: List<Person>? = null,
    val seasonInfo: List<SeasonInfo>? = null,
    val budget: CurrencyValue? = null,
    val top10: Int? = null,
    val top250: Int? = null,
    val totalSeriesLength: Int? = null,
    val seriesLength: Int? = null,
    val isSeries: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (other is Movie) return id == other.id
        return false
    }

    override fun hashCode(): Int {
        return 31*id
    }

}

data class Country(
    val name: String,
    val slug: String
)

data class ExternalId(
    val kpHD: String,
    val imdb: String,
    val tmdb: Int
)


data class Fact(
    val value: String,
    val type: String,
    val spoiler: Boolean
)

data class Rating(
    val kp: Double,
    val imdb: Double,
    val tmdb: Double,
    val filmCritics: Double,
    val russianFilmCritics: Double,
    val await: Double
)


data class Votes(
    val kp: Double,
    val imdb: Double,
    val tmdb: Double,
    val filmCritics: Double,
    val russianFilmCritics: Double,
    val await: Double
)


data class Logo(
    val url: String
)


data class ShortImage(
    val url: String,
    val previewUrl: String
)


data class ItemName(
    val name: String
)


data class Person(
    val id: Int,
    val photo: String,
    val name: String,
    val enName: String,
    val description: String,
    val profession: String
)


data class SeasonInfo(
    val number: Int,
    val episodesCount: Int
)


data class CurrencyValue(
    val value: Int,
    val currency: String
)


data class MovieList(
    val docs: MutableList<Movie>
)

data class PosterList(
    val docs: MutableList<Poster>
)

data class Poster(
    val createdAt: String,
    val height: Int,
    val id: String,
    val movieId: Int,
    val previewUrl: String,
    val type: String,
    val updatedAt: String,
    val url: String,
    val width: Int
)

