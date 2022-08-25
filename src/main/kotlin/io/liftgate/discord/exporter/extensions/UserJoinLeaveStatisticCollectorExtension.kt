package io.liftgate.discord.exporter.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.core.event.guild.MemberJoinEvent
import dev.kord.core.event.guild.MemberLeaveEvent
import io.liftgate.discord.exporter.prefixed
import io.prometheus.client.Gauge

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
object UserJoinLeaveStatisticCollectorExtension : Extension()
{
    override val name = "user-join-leave-statistic-collector"

    override suspend fun setup()
    {
        val userJoinGauge = Gauge.build()
            .name("users_joined".prefixed)
            .help("How many users have joined the guild.")
            .register()

        val userLeaveGauge = Gauge.build()
            .name("users_left".prefixed)
            .help("How many users have left the guild.")
            .register()

        event<MemberJoinEvent> {
            userJoinGauge.inc()
        }

        event<MemberLeaveEvent> {
            userLeaveGauge.inc()
        }
    }
}
