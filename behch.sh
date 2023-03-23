#!/usr/bin/env bash

SCOPE=$1
SERVER_PORT=0

set ex;

case $SCOPE in
    spring)
    SERVER_PORT=61004
    shift
    ;;
    vanilla)
    SERVER_PORT=62004
    shift
    ;;
    *)
    echo "Unknown test scope: $SCOPE"
    exit 1
    ;;
esac

echo "Run ghz in scope ${SCOPE} with port ${SERVER_PORT} ---- "
echo "Benchmark will be stopped after 1 minute ---- "

docker run --rm --network=host \
    --volume "${PWD}/proto/src/main/proto:/ghz/protos" \
    obvionaoe/ghz \
    --insecure \
    --duration=1m \
    --proto=/ghz/protos/comrade.proto \
    --name=${SCOPE} \
    --call=comrade.test.proto.Comrade.isComrade \
    --cpus=1 \
    --concurrency=1 \
    0.0.0.0:${SERVER_PORT}