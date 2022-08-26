package io.liftgate.discord.exporter.extensions

import io.liftgate.discord.exporter.prefixed
import io.prometheus.client.Gauge
import net.dv8tion.jda.api.JDA
import java.lang.Thread.sleep
import kotlin.concurrent.thread

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
fun JDA.configureWSCollectors()
{
    val websocketResponseTime = Gauge.build()
        .name("websocket_ping".prefixed)
        .help("Average discord websocket heartbeat response time in millis.")
        .register()

    thread {
        this.restPing.queue {
            websocketResponseTime
                .set(
                    it.toDouble()
                )
        }

        sleep(5000L)
    }

    println("Configured discord bot stat collectors!")
}
