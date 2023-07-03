<div align="center">

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/joinprotection?logo=modrinth&style=for-the-badge)](https://modrinth.com/plugin/joinprotection)
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/rockquiet/joinprotection?style=for-the-badge&logo=github)](https://github.com/rockquiet/JoinProtection/releases)

# JoinProtection

A plugin that protects players from mobs and damage for a set period of time after they join\
...with many configurable options.

</div>

## Features

- After joining the server the player is invincible for X seconds
    - Protection time can be extended via permission
- Toggle if entities target players with active join protection
- Disable damage...
    - ...by entities
    - ...by blocks
    - ...by anything else (fall damage, etc.)
- Cancel the join protection if the player...
    - ...attacks an entity
    - ...moves X blocks
    - ...breaks/places blocks
- Particles aura during active join protection
- Play a sound to players trying to attack others with active join protection
- Whitelist/Blacklist worlds to restrict the plugin's functionality
- Everything can be edited and disabled

## Commands & Permissions

`/joinprotection reload` ▶ Reloads the config\
↪ Permission: `joinprotection.reload`

`joinprotection.use` ▶ If the Player has join protection _(default for everyone)_

`joinprotection.plus-NUMBER` ▶ Extends the protection time by NUMBER amount of seconds

`joinprotection.bypass.cancel-on-move` ▶ Bypass the cancel on move option

`joinprotection.bypass.cancel-on-attack` ▶ Bypass the cancel on attack option

`joinprotection.bypass.cancel-on-block-interact` ▶ Bypass the cancel on block interact option

`joinprotection.bypass.world-list` ▶ Bypass the world-list option

`joinprotection.*` ▶ Grants all permissions of the plugin

`joinprotection.bypass.*` ▶ Grants all bypass permissions

## Download

Plugin jars are available in the [Releases](https://github.com/rockquiet/JoinProtection/releases) section,
on [Modrinth](https://modrinth.com/plugin/joinprotection)
and [Hangar](https://hangar.papermc.io/rockquiet/JoinProtection).

[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/cozy/available/modrinth_64h.png)](https://modrinth.com/plugin/joinprotection)
[![hangar](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/hangar_64h.png)](https://hangar.papermc.io/rockquiet/JoinProtection)