#!/system/bin/sh
BUSYBOX="/sbin/busybox"

LOGCAT_DIR="/data/krobot/logcat"

# create log dir.
if [ ! -d "$LOGCAT_DIR" ]; then
    $BUSYBOX mkdir $LOGCAT_DIR
    chmod 777 $LOGCAT_DIR
fi
file_name=`date +"%Y_%m_%d_%H_%M_%S"`
# write log.
echo start logcat service
logcat -b main -v time -f "$LOGCAT_DIR/$file_name.log" &