package blog.tsalikis.starwars.characters.domain

import arrow.core.Either

typealias ContentError<T> = Either<Errors, T>

sealed interface Errors {

    data object NoConnection : Errors

    data object Generic : Errors

    data object NotFound: Errors
}
