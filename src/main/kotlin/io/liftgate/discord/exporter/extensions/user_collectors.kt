package io.liftgate.discord.exporter.extensions

import dev.minn.jda.ktx.events.listener
import io.liftgate.discord.exporter.prefixed
import io.prometheus.client.Counter
import io.prometheus.client.Gauge
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
fun JDA.configureUserCollectors()
{
    val userJoinGauge = Counter.build()
        .name("users_joined".prefixed)
        .help("How many users have joined the guild.")
        .register()

    val userLeaveGauge = Counter.build()
        .name("users_left".prefixed)
        .help("How many users have left the guild.")
        .register()

    val userAmountGauge = Gauge.build()
        .name("users_total".prefixed)
        .help("How many users are in the guild.")
        .register()

    listener<GuildMemberJoinEvent> {
        userJoinGauge.inc()
        userAmountGauge.inc()
    }

    listener<GuildMemberRemoveEvent> {
        userLeaveGauge.inc()
        userAmountGauge.dec()
    }

    listener<ReadyEvent> {
        val guild = it.jda.guilds.first()

        userAmountGauge.set(
            guild.memberCount.toDouble()
        )
    }

    println("Configured user join/leave collectors!")
}
