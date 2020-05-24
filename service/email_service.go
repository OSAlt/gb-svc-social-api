package main

import (
	"context"

	"log"

	pb "github.com/OSAlt/geekbeacon/service/autogen"
	"github.com/OSAlt/geekbeacon/service/dbmodels/nixie"
)

func (s *GrpcServer) GetEmailContactList(ctx context.Context, filter *pb.GenericFilter) (*pb.EmailContactList, error) {
	db := s.sqlDatabase

	contacts, err := nixie.ContactForms().All(ctx, db)
	if err != nil {
		log.Println("failed to retrieve contacts", err)
	}
	items := []*pb.EmailContact{}

	for _, contact := range contacts {
		items = append(items, &pb.EmailContact{
			Email:       contact.Email,
			Description: contact.Description,
		})

	}

	return &pb.EmailContactList{
		EmailContacts: items,
	}, nil
}
