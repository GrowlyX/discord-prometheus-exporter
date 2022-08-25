package io.liftgate.discord.exporter

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import dev.kord.core.supplier.EntitySupplyStrategy
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import io.prometheus.client.exporter.HTTPServer

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
suspend fun main(vararg args: String)
{
    class AppConfiguration(parser: ArgParser)
    {
        val token by parser.storing(
            "--token", help = "Discord bot token."
        )

        val bindAddress by parser
            .storing(
                "--addr", help = "Address on which the HTTP server will listen ojn."
            )
            .default("0.0.0.0:9800")
    }

    val configuration = ArgParser(args)
        .parseInto(::AppConfiguration)

    ExtensibleBot(configuration.token) {
        intents {
            @OptIn(PrivilegedIntent::class)
            this.flags().plus(Intents.all)
        }

        cache {
            cachedMessages = 10_000
            defaultStrategy = EntitySupplyStrategy.cacheWithCachingRestFallback
        }
    }

    val bindAddress = configuration
        .bindAddress.split(":")

    val server = HTTPServer(
        bindAddress.first(), bindAddress[1].toInt()
    )

    Runtime.getRuntime().addShutdownHook(
        Thread { server.close() }
    )
}
