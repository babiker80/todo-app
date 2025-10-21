#!/bin/sh
npx --yes @openapitools/openapi-generator-cli generate -i openapi.json -g html -o .tmp
cp .tmp/index.html ./api-docs.html
rm -R .tmp
