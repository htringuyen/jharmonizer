#!/bin/bash

set -o errexit


arg1=$1
arg2=$2

./gradlew :kaligo-sample-app:run --args="$arg1 $arg2"

