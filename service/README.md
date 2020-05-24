go install \
    github.com/grpc-ecosystem/grpc-gateway/protoc-gen-grpc-gateway \
        github.com/grpc-ecosystem/grpc-gateway/protoc-gen-swagger \
            github.com/golang/protobuf/protoc-gen-go

## Update SQL Models

sqlboiler.toml controls the behavior you may need to run it multiple times for each schema. 


xo "postgres://postgres:secret@localhost:5432/socialdb?sslmode=disable" --schema nixe -o models/nixie
xo "postgres://postgres:secret@localhost:5432/socialdb?sslmode=disable" --schema config -o models/config
