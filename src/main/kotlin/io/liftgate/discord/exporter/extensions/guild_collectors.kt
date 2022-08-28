package io.liftgate.discord.exporter.extensions

import dev.minn.jda.ktx.events.listener
import io.liftgate.discord.exporter.prefixed
import io.prometheus.client.Gauge
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
fun JDA.configureGuildCollectors()
{
    val guildBoostGauge = Gauge.build()
        .name("guild_boosts_active".prefixed)
        .help("How many boosts this guild has.")
        .register()

    listener<GuildUpdateBoostCountEvent> {
        guildBoostGauge.set(it.newBoostCount.toDouble())
    }

    listener<ReadyEvent> {
        val guild = it.jda.guilds.first()

        guildBoostGauge.set(
            guild.boostCount.toDouble()
        )
    }

    println("Configured voice channel collectors!")
}
