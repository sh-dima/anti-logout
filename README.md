Anti-Dangerlog
==============

*Fork of [Anti-Combatlog](https://modrinth.com/project/rflThpVC), which is a fork of [AntiLogout](https://modrinth.com/project/gnfTfbZi)*

Overview
--------

Anti-Dangerlog is a server-side Fabric Minecraft mod that prevents players from escaping combat by logging out. When a player logs out during combat, their "body" remains online for a configurable amount of time, making combat logging impossible. The mod also provides a flexible `/afk` command to safely go AFK for farming or other purposes.

Features
--------

* Prevents combat logging: players who log out during combat remain in the world.
* `/afk` command: lets players log out with the account still online, with optional time limits.
* Configurable messages and timeouts.
* Permission-based command and feature access.

Installation
------------

1. Download the mod JAR and place it in your server's `mods` folder.
2. Start the server to generate the config file.
3. Edit `config/antilogout.toml` to customize settings if needed.

Configuration
-------------

All options are in `config/antilogout.toml`.

**Key options**:

* `disableAllLogouts`: Disable all logout protection features.
* `debug`: Enable debug logging.
* `afkMessage`: Message shown when a player is AFK.
* `afkCombatMessage`: Message shown if a player tries to go AFK while in combat.
* `afkBroadcastMessage`: Broadcast when a player goes AFK (`{player}` = name).
* `combatEnterMessage`: Message when entering combat.
* `combatEndMessage`: Message when leaving combat.
* `combatTimeout`: How long a player is considered in combat (seconds).
* `combatDisconnectMessage`: Message when a player disconnects during combat.

Commands
--------

* `/afk`  
  Set yourself AFK for the max time. (Permission level 0)
* `/afk time <seconds>`  
  Set yourself AFK for a specific time (`-1` for unlimited). (Permission level 0)
* `/afk players <targets> [time <seconds>]`  
  Set other players AFK. (Admin only, permission level 4)
* `/antilogout reload`  
  Reload the config file. (Admin only, permission level 4)
* `/antilogout status`  
  Show current config summary. (Admin only, permission level 4)

Permissions
-----------

* `antilogout.bypass.combat` — Bypass combat tagging.
* `antilogout.command.afk` — Use `/afk` (level 0).
* `antilogout.command.afk.time` — Set AFK time for yourself (level 0).
* `antilogout.command.afk.players` — Set other players AFK (admin only, level 4).
* `antilogout.command.antilogout` — Use `/antilogout` admin commands (level 4).
* `antilogout.command.antilogout.reload` — Reload config (level 4).
* `antilogout.command.antilogout.edit` — Edit config in-game (if enabled, level 4).

Example Usage
-------------

**Combat log prevention**: Players who log out during combat remain in the world for the configured timeout.

**AFK farming**: Use `/afk` to safely go AFK for farming or other purposes.

Video Showcase
--------------

* [Combat log prevention demo](https://user-images.githubusercontent.com/34912839/213432960-15d54218-8313-4470-868b-10eb78357764.mp4)
* [AFK farming demo](https://user-images.githubusercontent.com/34912839/213676495-f3125d24-d42d-4ee9-80d2-55f33d313aae.mp4)

For questions, suggestions, or issues, please open an issue on GitHub or contact the maintainer.

Links
-----

**Anti-Dangerlog**:

* [Source](https://github.com/sh-dima/anti-logout)
* [Modrinth](https://modrinth.com/project/3hMLwQGv)

**Anti-Combatlog**:

* [Source](https://github.com/nussico/AntiLogout)
* [Modrinth](https://modrinth.com/project/rflThpVC)

**AntiLogout**:

* [Source](https://github.com/samolego/AntiLogout)
* [Modrinth](https://modrinth.com/project/gnfTfbZi)
* [CurseForge](https://www.curseforge.com/minecraft/mc-mods/antilogout)

Disclaimer
----------

NOT AN OFFICIAL MINECRAFT PRODUCT. NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT.
