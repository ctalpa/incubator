#!/bin/sh

set -ex

dropdb planning
createdb planning
psql -c "create user abms with password 'planning';" || :
psql -c "grant all privileges on database planning to planning;"
psql planning -c "create schema abms authorization planning;"
psql planning -c "create extension if not exists postgis schema public;"

