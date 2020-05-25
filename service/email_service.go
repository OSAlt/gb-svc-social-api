package main

import (
	"context"

	"log"

	pb "github.com/OSAlt/geekbeacon/service/autogen"
	"github.com/OSAlt/geekbeacon/service/dbmodels/nixie"
	"github.com/volatiletech/sqlboiler/queries/qm"
)

func (s *GrpcServer) GetEmailContactList(ctx context.Context, filter *pb.GenericFilter) (*pb.EmailContactList, error) {
	db := s.sqlDatabase
	var contacts nixie.ContactFormSlice
	var err error

	if filter.Domain != "" {
		contacts, err = nixie.ContactForms(qm.Where("domain=?", filter.Domain)).All(ctx, db)
	} else {
		contacts, err = nixie.ContactForms().All(ctx, db)
	}

	if err != nil {
		log.Println("failed to retrieve contacts", err)
	}
	items := []*pb.EmailContact{}

	for _, contact := range contacts {
		entry := pb.EmailContact{
			Email:       contact.Email,
			Description: contact.Description,
		}
		if filter.Domain == "" {
			entry.Domain = contact.Domain
		}

		items = append(items, &entry)

	}

	return &pb.EmailContactList{
		EmailContacts: items,
	}, nil
}
