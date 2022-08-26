package io.liftgate.discord.exporter.extensions

import dev.minn.jda.ktx.events.listener
import io.liftgate.discord.exporter.prefixed
import io.prometheus.client.Counter
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
fun JDA.configureInviteCollectors()
{
    val userInviteGauge = Counter.build()
        .name("invites_created".prefixed)
        .help("How many invites have been created.")
        .register()

    listener<GuildInviteCreateEvent> {
        userInviteGauge.inc()
    }

    println("Configured user join/leave collectors!")
}
