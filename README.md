# ForgeSlack
Connects Minecraft Forge -> Slack

*Currently only supports Minecraft 1.7.10*

## Features
1. Posts Slack Messages when people in-game talk. Thanks to [MCAPI](https://mcapi.ca/), this includes a picture of the User.
2. Posts Slack Messages when people join/leave the server.
3. Posts Minecraft Messages in Slack. 

## Installation

1. Place in mods directory. Start/Stop server to generate Config file.
2. Create "Incoming WebHook" on Slack. Add the URL to the Config file.
3. Create "Outgoing WebHook" on Slack. Configure using the URL of your minecraft server with the port configured in the file. Example: http://33.33.33.33:8085/. Add the Token provided here to the Config file.