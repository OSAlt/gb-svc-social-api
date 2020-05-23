#!/usr/bin/env bash
DIR="proto"
FILE="testing.proto"
#FILE="hello.proto"
buildGoCode() 
{
  rm -fr temp client/autogen service/autogen; mkdir temp
  protoc -I $DIR $DIR/$FILE --go_out=plugins=grpc:temp
  rsync -a temp/ client/autogen/
  rsync -a temp/ service/autogen/
  rm -fr temp
}

buildJsCode() 
{
  rm -fr temp; mkdir temp 
#  protoc -I $DIR $DIR/$FILE --js_out=import_style=commonjs:temp
  protoc -I $DIR $DIR/$FILE --grpc-web_out=import_style=commonjs,mode=grpcwebtext:temp
  cp temp/*.js web/

}


buildGoCode
buildJsCode

