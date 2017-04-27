#!/bin/sh

set -ex

dropdb apms
createdb apms
psql -c "create user apms with password 'apms';" || :
psql -c "grant all privileges on database apms to apms;"
psql apms -c "create schema apms authorization apms;"
psql apms -c "create extension if not exists postgis schema public;"

