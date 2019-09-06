#!/bin/sh
cd records && npm run build && cd ..
rm -r package
rm package-clinic.zip
mkdir -p package/data-api
mkdir -p package/page
cp -r records/dist/* package/page
cp groovy-scripts/* package/data-api/
cp misc/lightapp.json package/lightapp.json

mv package/page/index.html package/page/pcClinic.html
zip -r package-clinic.zip package/

