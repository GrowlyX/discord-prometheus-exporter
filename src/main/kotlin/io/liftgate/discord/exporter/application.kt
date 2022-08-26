package io.liftgate.discord.exporter

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import dev.kord.core.Kord
import dev.kord.core.enableEvents
import dev.kord.gateway.Intent
import io.liftgate.discord.exporter.extensions.configureBotStatistics
import io.liftgate.discord.exporter.extensions.configureUserCollectors
import io.prometheus.client.exporter.HTTPServer
import kotlinx.coroutines.runBlocking

/**
 * @author GrowlyX
 * @since 8/25/2022
 */
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

suspend fun main(vararg args: String)
{
    val configuration = ArgParser(args)
        .parseInto(::AppConfiguration)

    val kord = Kord(configuration.token)

    kord.configureBotStatistics()
    kord.configureUserCollectors()

    val bindAddress = configuration
        .bindAddress.split(":")

    val server = HTTPServer(
        bindAddress.first(), bindAddress[1].toInt()
    )

    Runtime.getRuntime().addShutdownHook(Thread {
        runCatching {
            runBlocking {
                kord.logout()
            }
        }.onFailure {
            it.printStackTrace()
        }

        server.close()
    })

    kord.login {
        this.intents.plus(
            Intent.Guilds
        )

        intents {
            this.enableEvents(
                *registered.toTypedArray()
            )
        }

        println("Configured discord bot and registered all extensions.")
    }
}
