#!/usr/bin/env bash
DIR="proto"
FILE="testing.proto"
#FILE="hello.proto"
genGoCode() 
{
  rm -fr temp client/autogen service/autogen; mkdir temp
  #protoc -I $DIR $DIR/$FILE --go_out=plugins=grpc:temp
  protoc -I $DIR $DIR/$FILE --go_out=plugins=grpc,paths=source_relative:temp

  rsync -a temp/ client/autogen/
  rsync -a temp/ service/autogen/
  rm -fr temp
}

#genJsCode() 
#{
#  rm -fr temp; mkdir temp 
##  protoc -I $DIR $DIR/$FILE --js_out=import_style=commonjs:temp
#  protoc -I $DIR $DIR/$FILE --grpc-web_out=import_style=commonjs,mode=grpcwebtext:temp
#  cp temp/*.js web/
#
#}

genProxy()
{
  rm -fr temp; mkdir temp
  protoc -I $DIR $DIR/$FILE --grpc-gateway_out=logtostderr=true,paths=source_relative:temp  
  rsync -a temp/ service/autogen/
  rm -fr temp

}


genSwagger() 
{
  rm -fr temp; mkdir temp 
  protoc -I $DIR $DIR/$FILE --swagger_out=logtostderr=true:temp
  rsync -a temp/ service/

}


genGoCode
genProxy
genSwagger

