#!/usr/bin/env bash 
rm -fr models; mkdir -p models
sqlboiler -c config_autogen/sqlboiler_config_example.toml -p config psql
sqlboiler -c config_autogen/sqlboiler_nixie_example.toml -p nixie psql
