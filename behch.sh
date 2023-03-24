#!/usr/bin/env bash

SCOPE=$1
RESTRICTION=$2
SERVER_PORT=0
RESTRICT_MODE=''

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

case $RESTRICTION in
    1)
    RESTRICT_MODE='--cpus=1 --concurrency=1 '
    echo "Run with restriction by --cpus=1 --concurrency=1"
    shift
    ;;
    *)
    echo "Run without restriction by CPU"
    shift
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
    ${RESTRICT_MODE} 0.0.0.0:${SERVER_PORT}
