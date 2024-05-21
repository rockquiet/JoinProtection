<div align="center">

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/joinprotection?style=flat-square&logo=modrinth&label=Downloads&labelColor=29355F&color=009B98)](https://modrinth.com/plugin/joinprotection)
[![Spigot Downloads](https://img.shields.io/spiget/downloads/113320?style=flat-square&label=Downloads&logo=image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAAD1BMVEVHcEwAAAD/0ADi6D86RuhWkE5HAAAAAXRSTlMAQObYZgAAAFBJREFUeJxljdERgDAIQ8OdAxicgHQCZQH3n0pqrT99P7y7QADAYkfHiIHFJ4yRwDWlDaHy7IPeMupOUkvVFiu5XL3hyLBXjIT/nfPLdq/yAL5yBqT7qDihAAAAAElFTkSuQmCC&labelColor=29355F&color=009B98)](https://www.spigotmc.org/resources/113320)
[![Hangar Downloads](https://img.shields.io/hangar/dt/joinprotection?style=flat-square&logo=image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAAgBAMAAAAVss41AAAAJFBMVEVHcEz///////////////////////////////////////////8Uel1nAAAAC3RSTlMAH47fTa3EafI0C3ZKri0AAADwSURBVHicTZGvC8JQEMdPDeKSYBEsEwSFFbGJZcVkMRiEV7S6IgiCWNS6ZlBhxWZ3/ti8f87bfbfplePDvXff7/s+Iqlyr0p5FQy/3JwuzBw2s9GAk5qAaozqK3mAaKkL09FcR3XAE9cc0ELB8hVC6KfX4qtSCdQ+6LCB9TWYGyndt7LVzVbufQ7Pk8xJW9S9qEqwPORQ7Nop1Tk2zGNS8VfALWld0N3hk7QdTr7NM1DyQA9HT5qEHuZTmTLPaKvkzchaCQUgW0wOx3jD22goVypCwc2DFvVRlnWhIzk08uSttfj8/QMd/c3fJ9HNRv8CUjKn1XnSu4wAAAAASUVORK5CYII=&label=Downloads&labelColor=29355F&color=009B98)](https://hangar.papermc.io/rockquiet/JoinProtection)
[![GitHub Downloads](https://img.shields.io/github/downloads/rockquiet/joinprotection/total?style=flat-square&label=Downloads&logo=github&labelColor=29355F&color=009B98)](https://github.com/rockquiet/JoinProtection/releases)

[![GitHub release](https://img.shields.io/github/v/release/rockquiet/joinprotection?style=for-the-badge&labelColor=29355F&color=009B98)](https://github.com/rockquiet/JoinProtection/releases)
![Minecraft Versions](https://img.shields.io/badge/minecraft-1.17.1_--_1.20.4-009B98?style=for-the-badge&logoColor=blue&labelColor=29355F)
![Java](https://img.shields.io/badge/java-17+-009B98?style=for-the-badge&logoColor=blue&labelColor=29355F)

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
- Prevent the player from dropping/picking up items
- Cancel the join protection if the player...
    - ...attacks an entity
    - ...moves X blocks
    - ...breaks/places blocks
- Particles aura during active join protection
- Play a sound to players trying to attack others with active join protection
- Whitelist/Blacklist worlds to restrict the plugin's functionality
- [PlaceholderAPI](https://www.spigotmc.org/resources/6245)
  and [MiniPlaceholders](https://modrinth.com/plugin/miniplaceholders) support
- [LuckPerms Context](https://luckperms.net/wiki/Context) support
- Everything can be edited, bypassed with permissions, or completely disabled

## Commands & Permissions

| command                  | permission              | description        |
|--------------------------|-------------------------|--------------------|
| `/joinprotection reload` | `joinprotection.reload` | Reloads the config |

| permission                                       | default | description                                                                                                                              |
|--------------------------------------------------|---------|------------------------------------------------------------------------------------------------------------------------------------------|
| `joinprotection.use`                             | true    | If the Player has join protection                                                                                                        |
| `joinprotection.reload`                          | op      | Allows the usage of `/joinprotection reload`                                                                                             |
| `joinprotection.plus-NUMBER`                     | false   | Extends the protection time by NUMBER amount of seconds<br/>(If a player has multiple permissions, only the highest value will be added) |
| `joinprotection.bypass.cancel-on-move`           | false   | Bypass the cancel on move option                                                                                                         |
| `joinprotection.bypass.cancel-on-attack`         | false   | Bypass the cancel on attack option                                                                                                       |
| `joinprotection.bypass.cancel-on-block-interact` | false   | Bypass the cancel on block interact option                                                                                               |
| `joinprotection.bypass.world-list`               | false   | Bypass the world-list option                                                                                                             |
| `joinprotection.bypass.*`                        | false   | Grants all bypass permissions                                                                                                            |
| `joinprotection.*`                               | false   | Grants all permissions of the plugin                                                                                                     |

## Placeholders

| [PlaceholderAPI](https://www.spigotmc.org/resources/6245) | [MiniPlaceholders](https://modrinth.com/plugin/miniplaceholders) | description                     |
|-----------------------------------------------------------|------------------------------------------------------------------|---------------------------------|
| `%joinprotection_status%`                                 | `<joinprotection_status>`                                        | Protection status of the player |

## LuckPerms Context

The plugin updates an `joinprotection` context for each player:\
It can be either `true` (player is protected) or `false` (player is not protected).

More information on how it works is available on the [LuckPerms Wiki](https://luckperms.net/wiki/Context).

## Download

Plugin jars are available in the [GitHub releases](https://github.com/rockquiet/JoinProtection/releases) section,
on [Modrinth](https://modrinth.com/plugin/joinprotection), [Spigot](https://www.spigotmc.org/resources/113320)
and [Hangar](https://hangar.papermc.io/rockquiet/JoinProtection).

[<img alt="github" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/github_vector.svg">](https://github.com/rockquiet/JoinProtection/releases)
[<img alt="modrinth" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg">](https://modrinth.com/plugin/joinprotection)
[<img alt="spigot" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/spigot_vector.svg">](https://www.spigotmc.org/resources/113320)
[<img alt="hangar" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/hangar_vector.svg">](https://hangar.papermc.io/rockquiet/JoinProtection)

## Metrics

This plugin uses bStats to collect some (non-identifying) data about the servers it runs on.
You can opt out by editing the `config.yml` in the `/plugins/bStats` folder located in your server directory.
(More information [here](https://bstats.org/getting-started))

[<img alt="bstats" src="https://bstats.org/signatures/bukkit/JoinProtection.svg">](https://bstats.org/plugin/bukkit/JoinProtection)
