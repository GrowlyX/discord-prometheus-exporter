package io.liftgate.discord.exporter.extensions

import dev.minn.jda.ktx.events.listener
import io.liftgate.discord.exporter.prefixed
import io.prometheus.client.Counter
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
fun JDA.configureReactionCollectors()
{
    val reactionCounter = Counter.build()
        .name("reactions_added".prefixed)
        .help("How many reactions have been added.")
        .register()

    val reactionRemovalCounter = Counter.build()
        .name("reactions_removed".prefixed)
        .help("How many reactions have been removed.")
        .register()

    listener<MessageReactionAddEvent> {
        reactionCounter.inc()
    }

    listener<MessageReactionAddEvent> {
        reactionRemovalCounter.inc()
    }

    println("Configured user join/leave collectors!")
}
