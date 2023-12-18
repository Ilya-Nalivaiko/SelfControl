# Self Control, for those without
This plugin adds a way to enforce your "I will log off by X time", by automatically kicking, or even temp-banning you at a specified time.

## Usage
- `kickme at [time]` or `banme at [time]` to forcefully log off at that time
- `kickme r` or `banme r` to cancel the request

## Config
- Custom messages for the kick/ban request being executed
- Set the temp-ban time (1 hour by default).
- Change the time zone to match where you expect your players to be, not where the server is
- The format of the time entry can be changed, though some formats may produce unexpected behaviour

## Current limitations (may be future features)
- Only one one-time request exists per user (no repeated, scheduled tasks)
- The temp-ban time is specified the same for all players in the config
- Players will have to specify the time they should be kicked in the time zone set in the config only

## Inherent limitations (will most likely not be fixed)
- The temp-ban feature requires Essentials or similar plugin, as this is not a vanilla feature
- This plugin shows the time in real time, but it actually works in game ticks. If your server is lagging (the TPS is below 20) the time when the task is executed will be delayed
