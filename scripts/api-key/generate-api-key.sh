#!/bin/bash
docker build . -t api-key-generator && docker run --rm -it api-key-generator
