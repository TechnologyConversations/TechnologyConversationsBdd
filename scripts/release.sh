#!/bin/bash

mkdir -p release/bdd-assistant
cp -R composites conf data LICENCE.txt public steps target release//bdd-assistant/.
cd release
rm -rf release/bdd-assistant
tar -czf relese.tar.gz bdd-assistant
