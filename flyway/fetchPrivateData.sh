#!/usr/bin/env bash
#BUCKET=""
PROFILE="personal"

aws s3 --profile $PROFILE cp s3://$1/social-rest/sql/V999__private.sql sql/
