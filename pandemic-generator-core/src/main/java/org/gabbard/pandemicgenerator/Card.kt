package org.gabbard.pandemicgenerator

import java.util.*

sealed class Card {
    abstract val userString: String
}

data class InfectionCard(val city: City) : Card() {
    override val userString: String
        get() = city.name

    override fun toString(): String = userString
}

sealed class PlayerCard : Card()

data class CityPlayerCard(val city: City) : PlayerCard() {
    override val userString: String
        get() = city.name

    override fun toString(): String = city.toString()
}

sealed class Epidemic : PlayerCard()

class SimpleEpidemic : Epidemic() {
    override val userString = "Epidemic"
    override fun toString(): String = userString
}

class NamedEpidemic(val name: String) : Epidemic() {
    override val userString: String
        get() = name

    override fun toString(): String = userString
}

class EventCard(val name: String) : PlayerCard() {
    override val userString: String
        get() = name

    override fun toString(): String = userString
}

data class Deck<CardType : Card>(val cards: List<CardType>) {
    fun shuffled(rng: Random) = Deck(cards.shuffled(rng))
    fun draw(numToDraw: Int): Pair<List<CardType>, Deck<CardType>> {
        require(numToDraw <= cards.size)
        return Pair(cards.subList(0, numToDraw), Deck(cards.subList(numToDraw, cards.size)))
    }

    fun drawOneFromTheBottom(): Pair<CardType, Deck<CardType>> {
        require(cards.isNotEmpty())
        return Pair(cards.last(), Deck(cards.subList(0, cards.size - 1)))
    }

    fun placeOnTopOf(otherDeck: Deck<CardType>): Deck<CardType> {
        return Deck(cards.plus(otherDeck.cards))
    }
}

val CHAMPIONSHIP_VIRULENT_STRAIN_EPIDEMICS = setOf("Chronic Effect", "Unacceptable Loss",
        "Government Interference", "Complex Molecular Structure", "Slippery Slope",
        "Uncounted Populations").map { NamedEpidemic(it) }.toSet()

val VIRULENT_STRAIN_EPIDEMICS = setOf("Hidden Pocket", "Rate Effect").map { NamedEpidemic(it) }
        .union(CHAMPIONSHIP_VIRULENT_STRAIN_EPIDEMICS).toSet()

val COMPETITIVE_PLAY_EVENTS = setOf("Borrowed Time", "Special Orders", "Mobile Hospital",
        "Airlift", "Rapid Vaccine Deployment", "Re-examined Research", "Remote Treatrment",
        "Government Grant").map { EventCard(it) }.toSet()
