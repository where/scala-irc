#!/usr/bin/env bash
#----------------------
# daemon.sh - test run of the IRC app.

# Add the resources directory to get the correct akka.conf
classpath=target/scala_2.8.1/resources/:target/scala_2.8.1/classes/
for lib in lib_managed/scala_2.8.1/compile/*
do
		classpath=$classpath:$lib
done

scala -classpath $classpath daemon.scala
