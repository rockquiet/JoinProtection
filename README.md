<div align="center">

# JoinProtection

Timed join protection plugin with many configurable options.

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
- Particles aura during active join protection
- Play a sound to players trying to attack others with active join protection
- Everything can be edited and disabled

## Commands & Permissions

`/joinprotection reload` ▶ Reloads the config

↪ Permission: `joinprotection.reload`

`joinprotection.use` ▶ If the Player has join protection _(default for everyone)_

`joinprotection.plus-NUMBER` ▶ Extends the protection time by NUMBER amount of seconds

`joinprotection.bypass.cancel-on-move` ▶ Bypass the cancel on move option

`joinprotection.bypass.cancel-on-attack` ▶ Bypass the cancel on attack option