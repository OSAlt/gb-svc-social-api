Please install flyway see: https://flywaydb.org/getstarted/firststeps/commandline for more info.

Example syntax:
    flyway -configFiles=conf/flyway.conf migrate

Secret data is store on AWS s3.  Either create your own temporary sql that isn't in git or fetch from your own external storage so
that sensitive data is not pushed to a public version control.
