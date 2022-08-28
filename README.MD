# Discord Prometheus Exporter
Simple metrics exporter HTTP server for Prometheus meant to be used for private Discord servers.

Thanks to the creator of [promcord](https://github.com/nimarion/promcord) for the original idea! This project is meant to be a simpler replacement for it.

## Metrics tracked
 - **Application:**
   - Discord websocket response time
 - **Guild:**
   - The active boost count.
 - **Voice channels:**
   - How many joins there have been
   - How many users are in a voice channel
 - **User count:**
   - How many users have joined/left
   - How many users there are in total
 - **Reactions:**
   - How many reactions have been added/removed
 - **Messages:**
   - How many messages have been created, updated, and deleted
 - **Invites:**
   - How many invites have been created
   - How many invites there are in total

## Build & Run
Run `./gradlew cleanBuild`. The final jar will be available in the `build/libs` directory.

Discord prometheus exporter requires you to pass through your Discord bot token in the application's start parameters. You may start the application with the following flags:
 - `--token` _(required)_: your Discord bot token
 - `--addr` _(default: `0.0.0.0:9800`)_: the address our HTTP server will bind to

# Dashboard
We do not have a dashboard template yet :(. Come back later!
