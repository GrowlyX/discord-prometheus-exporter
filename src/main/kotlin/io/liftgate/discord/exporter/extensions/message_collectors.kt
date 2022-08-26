package io.liftgate.discord.exporter.extensions

import dev.minn.jda.ktx.events.listener
import io.liftgate.discord.exporter.prefixed
import io.prometheus.client.Counter
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
fun JDA.configureMessageCollectors()
{
    val messagesSentGauge = Counter.build()
        .name("messages_sent".prefixed)
        .help("Amount of messages which have been sent.")
        .register()

    val messagesUpdatedGauge = Counter.build()
        .name("messages_updated".prefixed)
        .help("Amount of messages which have been updated.")
        .register()

    val messagesDeletedGauge = Counter.build()
        .name("messages_deleted".prefixed)
        .help("Amount of messages which have been deleted.")
        .register()

    listener<MessageReceivedEvent> {
        messagesSentGauge.inc()
    }

    listener<MessageUpdateEvent> {
        messagesUpdatedGauge.inc()
    }

    listener<MessageDeleteEvent> {
        messagesDeletedGauge.inc()
    }

    println("Configured message collectors!")
}
