'use strict';

// Prerequiste uuidv4
const {uuid} = require('uuidv4');

// Prerequiste bcrypt
let bcrypt
try {
    bcrypt = require('bcryptjs');
} catch (err) {
    process.stderr.write('Bcryptjs not installed. Do a \'npm i bcryptjs\'\n');
    process.exit(1);
}

/**
 *      Command line tool to create and uuid / api key pair
 *
 *      Use: node api-key-generator
 *      Returns something like:
 *      8384b34b-42dc-4e8b-96ea-469727ec3975
 *      $2a$10$yAmNdSM7WENHPjOW2TGSruxDWWKB3l66CuV88qnDkOhfcKshtK.IS
 */

function generate() {
    var id = uuid();
    var salt = bcrypt.genSaltSync(12);
    var hash = bcrypt.hashSync(id, salt);
    process.stdout.write(id.toString());
    process.stdout.write('\n');
    process.stdout.write(hash.toString());
    process.stdout.write('\n');
    process.exit(0);
}

generate();
