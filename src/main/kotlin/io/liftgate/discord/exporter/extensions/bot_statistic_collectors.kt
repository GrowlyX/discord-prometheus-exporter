package io.liftgate.discord.exporter.extensions

import dev.kord.core.Kord
import io.liftgate.discord.exporter.prefixed
import io.prometheus.client.Gauge
import kotlin.system.measureTimeMillis

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
suspend fun Kord.configureBotStatistics()
{
    val websocketResponseTime = Gauge.build()
        .name("websocket_ping".prefixed)
        .help("Average discord websocket heartbeat response time in millis.")
        .register()

    // please tell me there's a better way to do this
    val millis = measureTimeMillis {
        println("Listening on ${
            getApplicationInfo().name
        }")
    }

    websocketResponseTime
        .set(
            millis.toDouble()
        )

    println("Configured discord bot stat extensions!")
}
