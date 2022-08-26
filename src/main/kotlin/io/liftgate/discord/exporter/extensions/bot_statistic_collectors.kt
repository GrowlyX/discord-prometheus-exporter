package io.liftgate.discord.exporter.extensions

import io.liftgate.discord.exporter.prefixed
import io.prometheus.client.Gauge
import net.dv8tion.jda.api.JDA

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
fun JDA.configureRestCollectors()
{
    val websocketResponseTime = Gauge.build()
        .name("websocket_ping".prefixed)
        .help("Average discord websocket heartbeat response time in millis.")
        .register()

    this.restPing.queue {
        websocketResponseTime
            .set(
                it.toDouble()
            )
    }

    println("Configured discord bot stat collectors!")
}
