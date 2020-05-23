package main

import (
	"context"

	pb "github.com/OSAlt/geekbeacon/service/autogen"
)

func (s *GrpcServer) GetEmailContactList(ctx context.Context, filter *pb.GenericFilter) (*pb.EmailContactList, error) {

	items := []*pb.EmailContact{}
	items = append(items, &pb.EmailContact{
		Email:       "samir@esamir.com",
		Description: "hello world",
	})

	return &pb.EmailContactList{
		EmailContacts: items,
	}, nil
}
