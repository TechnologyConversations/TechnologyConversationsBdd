#!/bin/bash

rm -rf release
mkdir release
cp -R composites conf data LICENCE.txt public steps target release/.
cd release
tar -czf relese.tar.gz *
