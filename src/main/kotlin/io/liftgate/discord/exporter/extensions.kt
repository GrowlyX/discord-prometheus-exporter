package io.liftgate.discord.exporter

import dev.kord.core.event.Event
import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
val String.prefixed: String
    get() = "discord_server_$this"

val registered = mutableSetOf<KClass<out Event>>()
