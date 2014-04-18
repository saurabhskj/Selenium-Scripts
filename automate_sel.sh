#!/bin/bash
gnome-terminal --title="Hub Control" -x bash -c "ant launch-hub"
sleep 100
gnome-terminal --tab --title="Script environment firefox" -x bash -c "ant -Dport=5556 -Denvironment=*firefox launch-remote-control" &amp;
gnome-terminal --tab --title="Script env. chrome" -x bash -c "ant -Dport=5557 -Denvironment=*chrome lanuch-remote-contorl" &amp;

