#!/bin/sh

nohup java -jar -Xms2048M -Xmx4096M -XX:ErrorFile=logs/hs_err_pid.log -XX:+PrintGC -Xloggc:logs/gc.log crawler-engine-1.0.jar 2>&1 &
echo $! > pid
