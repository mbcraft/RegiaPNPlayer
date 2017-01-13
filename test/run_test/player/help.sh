#!/bin/sh

#
# Some help from :
# http://stackoverflow.com/questions/242538/unix-shell-script-find-out-which-directory-the-script-file-resides
# http://stackoverflow.com/users/31099/themarko
#

cd $(dirname "$0")

echo "help.sh -> prints this help\n"
echo "update.sh -> forces an update of the player\n"
echo "reload.sh -> forces a reload of the configuration files\n"
echo "start.sh -> starts an instance, if not already started\n"
echo "stop.sh -> stops a currently running instance\n"
echo "status.sh -> prints the status to the log file\n"
echo "restart.sh -> restarts the instance\n"
