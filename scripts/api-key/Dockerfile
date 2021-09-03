FROM node:15-alpine

WORKDIR /usr/src/app

COPY package.json package.json

RUN npm install

COPY api-key-generator.js api-key-generator.js

ENTRYPOINT ["node", "api-key-generator.js"]