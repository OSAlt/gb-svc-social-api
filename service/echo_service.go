package main

import (
	"context"

	pb "github.com/OSAlt/geekbeacon/service/autogen"
)

func (s *GrpcServer) Echo(ctx context.Context, in *pb.EchoMessage) (*pb.EchoMessage, error) {
	return in, nil
}
