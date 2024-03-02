#!/usr/bin/env sh
set -e

adb reverse tcp:8080 tcp:8080
caddy run --watch
