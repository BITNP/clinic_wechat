#!/bin/sh
cd records && npm run build && cd ..
rm -r package
rm package-clinic.zip
mkdir -p package/data-api
mkdir -p package/page
cp -r records/dist/* package/page
cp groovy-scripts/* package/data-api/
bash rename-api-key.sh # you should defile the file by yourself to change apikey in groovy script
cp misc/lightapp.json package/lightapp.json

mv package/page/index.html package/page/pcClinic.html
zip -r package-clinic.zip package/

