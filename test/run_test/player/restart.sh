#!/bin/sh

#
# Some help from :
# http://stackoverflow.com/questions/242538/unix-shell-script-find-out-which-directory-the-script-file-resides
# http://stackoverflow.com/users/31099/themarko
#

cd $(dirname "$0")

if test -f tmp/flags/instance.run
then touch tmp/flags/restart.now
else echo "Instance was not running ... starting now ..." && sh __start_impl.sh &
fi
