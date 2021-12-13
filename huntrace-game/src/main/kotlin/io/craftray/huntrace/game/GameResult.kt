package io.craftray.huntrace.game

enum class GameResult {
    /**
     * Basically survivors died
     */
    HUNTER_WIN,

    /**
     * Basically the end dragon was killed
     */
    SURVIVOR_WIN,

    /**
     * When all survivors quited
     */
    SURVIVOR_QUIT,

    /**
     * When all hunters quited
     */
    HUNTER_QUIT,

    /**
     * WHen the game was aborted manually
     */
    ABORT
}