package io.liftgate.discord.exporter.extensions

import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.guild.MemberJoinEvent
import dev.kord.core.event.guild.MemberLeaveEvent
import dev.kord.core.on
import io.liftgate.discord.exporter.AppConfiguration
import io.liftgate.discord.exporter.prefixed
import io.liftgate.discord.exporter.registered
import io.prometheus.client.Gauge
import kotlinx.coroutines.flow.count

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
suspend fun Kord.configureUserCollectors()
{
    val userJoinGauge = Gauge.build()
        .name("users_joined".prefixed)
        .help("How many users have joined the guild.")
        .register()

    val userLeaveGauge = Gauge.build()
        .name("users_left".prefixed)
        .help("How many users have left the guild.")
        .register()

    val userAmountCollectors = Gauge.build()
        .name("users_total".prefixed)
        .help("How many users have left the guild.")
        .register()

    registered += ReadyEvent::class
    registered += MemberJoinEvent::class
    registered += MemberLeaveEvent::class

    on<MemberJoinEvent> {
        userJoinGauge.inc()
        userAmountCollectors.inc()
    }

    on<MemberLeaveEvent> {
        userLeaveGauge.inc()
        userAmountCollectors.dec()
    }

    on<ReadyEvent> {
        val guild = this.guilds.first()
        val count = guild.members.count()

        userAmountCollectors.set(count.toDouble())
    }

    println("Configured user join/leave extensions!")
}
