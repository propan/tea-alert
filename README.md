# tea-alert [![Build Status](https://travis-ci.org/propan/tea-alert.svg?branch=master)](https://travis-ci.org/propan/tea-alert)

a notification system that informs you when new stuff is up for sale in your favourite tea store

## Building

1. Checkout sources
2. Build with `lein uberjar`

## Configuration

The service requires the following environment variables:

 - `MAILJET_KEY` - a [Mailjet](https://www.mailjet.com) key that is used for sending notification emails
 - `MAILJET_SECRET`- a [Mailjet](https://www.mailjet.com) secret that is used for sending notification emails
 - `SENDER_EMAIL` - an email that is configured in Mailjet and is used as a sender of notifications
 - `RECIPIENTS` - a comma separated list of notification recipients. Addresses must follow [RFC822](https://www.w3.org/Protocols/rfc822/) syntax.
 - `ALERT_RECIPIENTS` - a comma separated list of alerts recipients. Addresses must follow [RFC822](https://www.w3.org/Protocols/rfc822/) syntax.

## Usage

Run `java -jar tea-alert-standalone.jar`

## License

Copyright © 2016-2017 Pavel Prokopenko

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.

