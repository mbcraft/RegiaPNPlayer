#!/bin/sh

java -cp dist.lib.* -jar dist/RegiaPNPlayer.jar --start

while test -f tmp/reboot.now
do java -cp dist.lib.* -jar dist/RegiaPNPlayer.jar --start
done
