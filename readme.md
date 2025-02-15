# Watchpup Plugin for Fabric Minecraft

Creates a lightweight web server that returns only the current player count, nothing else.

Default bind address: 0.0.0.0
Default port: 57475

For use with a watchdog service or other monitoring tools.

A [Watchpup Paper Plugin](https://github.com/j1mmie/minecraft-watchpup-paper) is also available for Paper users.

This is used by [Jimmie's Minecraft OnDemand](https://github.com/j1mmie/minecraft-ondemand) fork which provides more reliable monitoring than the original.

## Build

```bash
sdk env
gradle wrapper # If you haven't already done this
./gradlew build
```

Output will be at:
```
build/libs/watchpup-fabric-x.y.z.jar
```
