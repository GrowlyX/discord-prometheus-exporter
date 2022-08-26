package io.liftgate.discord.exporter

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import dev.minn.jda.ktx.jdabuilder.default
import dev.minn.jda.ktx.jdabuilder.intents
import io.liftgate.discord.exporter.extensions.configureRestCollectors
import io.liftgate.discord.exporter.extensions.configureMessageCollectors
import io.liftgate.discord.exporter.extensions.configureInviteCollectors
import io.liftgate.discord.exporter.extensions.configureReactionCollectors
import io.liftgate.discord.exporter.extensions.configureUserCollectors
import io.prometheus.client.exporter.HTTPServer
import net.dv8tion.jda.api.requests.GatewayIntent

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

fun main(vararg args: String)
{
    val configuration = ArgParser(args)
        .parseInto(::AppConfiguration)

    val discord = default(
        configuration.token, enableCoroutines = true
    ) {
        this.intents += GatewayIntent.values().toList()
    }

    discord.configureRestCollectors()
    discord.configureRestCollectors()
    discord.configureReactionCollectors()
    discord.configureInviteCollectors()
    discord.configureMessageCollectors()

    val bindAddress = configuration
        .bindAddress.split(":")

    val server = HTTPServer(
        bindAddress.first(), bindAddress[1].toInt()
    )

    Runtime.getRuntime()
        .addShutdownHook(Thread {
            runCatching {
                discord.shutdownNow()
            }.onFailure {
                it.printStackTrace()
            }

            server.close()
        })

    discord.awaitReady()

    println("Configured discord bot and registered all extensions.")
}
