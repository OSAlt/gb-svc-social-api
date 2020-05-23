package main

import (
	"context"
	"strings"

	pb "github.com/OSAlt/geekbeacon/service/autogen"
)

func (s *GrpcServer) CreateCustomer(ctx context.Context, in *pb.CustomerRequest) (*pb.CustomerResponse, error) {
	s.savedCustomers = append(s.savedCustomers, in)
	return &pb.CustomerResponse{Id: in.Id, Success: true}, nil
}

func (s *GrpcServer) GetCustomers(ctx context.Context, filter *pb.CustomerFilter) (*pb.CustomerList, error) {
	items := []*pb.CustomerRequest{}
	for _, customer := range s.savedCustomers {
		if filter.Keyword != "" {
			if !strings.Contains(customer.Name, filter.Keyword) {
				continue
			}
		}
		items = append(items, customer)
	}

	return &pb.CustomerList{
		Customers: items,
	}, nil
}
