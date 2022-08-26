package io.liftgate.discord.exporter.extensions

import dev.kord.core.Kord
import dev.kord.core.event.guild.MemberJoinEvent
import dev.kord.core.event.guild.MemberLeaveEvent
import dev.kord.core.on
import io.liftgate.discord.exporter.prefixed
import io.liftgate.discord.exporter.registered
import io.prometheus.client.Gauge

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
fun Kord.configureUserJoinLeave()
{
    val userJoinGauge = Gauge.build()
        .name("users_joined".prefixed)
        .help("How many users have joined the guild.")
        .register()

    val userLeaveGauge = Gauge.build()
        .name("users_left".prefixed)
        .help("How many users have left the guild.")
        .register()

    registered += MemberJoinEvent::class
    registered += MemberLeaveEvent::class

    on<MemberJoinEvent> {
        userJoinGauge.inc()
    }

    on<MemberLeaveEvent> {
        userLeaveGauge.inc()
    }

    println("Configured user join/leave extensions!")
}
