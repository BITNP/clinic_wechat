#!/bin/sh
rm -r package
mkdir -p package/data-api
mkdir -p package/page
cp -r records/dist/* package/page
cp groovy-scripts/* package/data-api/
cp misc/lightapp.json package/lightapp.json