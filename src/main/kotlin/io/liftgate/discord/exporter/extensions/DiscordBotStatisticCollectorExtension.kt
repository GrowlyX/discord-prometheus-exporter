package io.liftgate.discord.exporter.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import io.liftgate.discord.exporter.prefixed
import io.prometheus.client.Gauge
import org.koin.core.component.KoinComponent
import java.lang.Thread.sleep
import kotlin.concurrent.thread
import kotlin.time.ExperimentalTime

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
object DiscordBotStatisticCollectorExtension : Extension(), KoinComponent
{
    override val name = "discord-bot-statistic-collector"

    override suspend fun setup()
    {
        val websocketResponseTime = Gauge.build()
            .name("websocket_ping".prefixed)
            .help("Average discord websocket heartbeat response time in millis.")
            .register()

        thread {
            kotlin.runCatching {
                @OptIn(ExperimentalTime::class)
                websocketResponseTime.set(
                    kord.gateway.averagePing?.inMilliseconds ?: 0.0
                )
            }.onFailure {
                it.printStackTrace()
            }

            sleep(1000)
        }
    }
}
