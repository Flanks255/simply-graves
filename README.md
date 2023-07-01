# What is Simply Graves?

Simply Graves is a lightweight gravestone mod.
Run into trouble and die? simply run back to your grave, and right click it to recover your items.

## Features
- Configurable opt-out/in system.
- 12 Different material themes for the graves, chosen at random.
- Graves are protected for a configurable amount of time, afterward anyone can gather the items.
- If a grave fails to spawn(you died in a wall etc.) your items are still safe, and can be recovered easily.
- Commands for operators to forcibly recover graves.

## Command list
-  /simplygraves latest
  - Lists information about your latest death and grave location.
- /simplygraves failed
  - Recovers inventory from a grave that failed to spawn.
- /simplygraves option (enable/disable)
  - Sets your grave preference.
- /simplygraves option [Player-Name] (enable/disable)
  - OP command to set a players' preference.
- /simplygraves list
  - OP command to list all graves
- /simplygraves recover [Grave-UUID] [Player-Name]
  - OP command to forcibly recover a specified grave to a targeted player.
- all commands can be used with /sg as well.


## Config options
- defaultOption
    - The default grave option for players, Default: **true**
- operatorOnly
    - If true, only OP's can set grave preference, Default: **false**
- publicDelay
    - The delay in seconds a grave is protected, afterward the grave becomes public, and anyone can collect it, Default **3600** (1 hour)


## Future plans.
- User preferences for grave material.
- Decorative versions of the graves.