package io.liftgate.discord.exporter.extensions

import dev.minn.jda.ktx.events.listener
import io.liftgate.discord.exporter.prefixed
import io.prometheus.client.Counter
import io.prometheus.client.Gauge
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
fun JDA.configureVoiceChannelCollectors()
{
    val voiceChannelGauge = Gauge.build()
        .name("voice_channel_active".prefixed)
        .help("How many voice voice channel participants there are.")
        .register()

    val voiceChannelEntered  = Counter.build()
        .name("voice_channel_entered".prefixed)
        .help("How many users have entered a voice channel.")
        .register()

    listener<GuildVoiceJoinEvent> {
        voiceChannelEntered.inc()
        voiceChannelGauge.inc()
    }

    listener<GuildVoiceLeaveEvent> {
        voiceChannelGauge.dec()
    }

    listener<ReadyEvent> {
        val guild = it.jda.guilds.first()

        voiceChannelGauge.set(
            guild.voiceChannels
                .sumOf { voice -> voice.members.size }
                .toDouble()
        )
    }

    println("Configured voice channel collectors!")
}
