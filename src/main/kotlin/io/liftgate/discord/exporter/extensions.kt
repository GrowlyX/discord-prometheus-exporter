package io.liftgate.discord.exporter

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
val String.prefixed: String
    get() = "discord_server_$this"
