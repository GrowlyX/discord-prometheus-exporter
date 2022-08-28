package io.liftgate.discord.exporter.extensions

import dev.minn.jda.ktx.events.listener
import io.liftgate.discord.exporter.prefixed
import io.prometheus.client.Counter
import io.prometheus.client.Gauge
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
fun JDA.configureInviteCollectors()
{
    val userInviteCounter = Counter.build()
        .name("invites_created".prefixed)
        .help("How many invites have been created.")
        .register()

    val userInviteGauge = Gauge.build()
        .name("invites_total".prefixed)
        .help("How many invites there are.")
        .register()

    listener<GuildInviteCreateEvent> {
        userInviteCounter.inc()
        userInviteGauge.inc()
    }

    listener<GuildInviteDeleteEvent> {
        userInviteGauge.dec()
    }

    listener<ReadyEvent> {
        val guild = it.jda.guilds.first()

        guild.retrieveInvites().queue { invites ->
            userInviteGauge.set(invites.size.toDouble())
        }
    }

    println("Configured invite collectors!")
}
