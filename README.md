# mcCamTools
Create minecraft cinematics for minecraft maps

This project is divided into two parts: th client side mod and the standalone java program.
Using the minecraft mod you can record the cinamatics and save them to a file.
The standalone app can create function/advancements using that file to replay that cinematic.

## Download

https://github.com/simonmeusel/mcCamTools/releases

## Mod

Install this mod using forge! You then have a new client side command: `/camtools`. It record your motion and saves it.

```
/camtools <delay in ticks> <duration in ticks> <path to output file>

/camtools 40 100 /path/cam.txt
```

## Standalone

Open the jar and follow the instuctions. Example:

```
advancements
relative: no
tp @a[tag=animation] %x %y %z %yaw %pitch\nsay Animation frame %i
/path/cam.txt
```
