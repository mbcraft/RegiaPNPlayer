#!/bin/sh

#
# Some help from :
# http://stackoverflow.com/questions/242538/unix-shell-script-find-out-which-directory-the-script-file-resides
# http://stackoverflow.com/users/31099/themarko
#

cd $(dirname "$0")

if test -f tmp/flags/instance.run 
then echo "Instance already running.";
else 
  ./__start_impl.sh
  while test -f tmp/flags/restart.now
  do ./__start_impl.sh
  done
fi

