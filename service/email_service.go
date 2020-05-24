package main

import (
	"context"
	"log"

	pb "github.com/OSAlt/geekbeacon/service/autogen"
)

func (s *GrpcServer) GetEmailContactList(ctx context.Context, filter *pb.GenericFilter) (*pb.EmailContactList, error) {
	rows, err := s.sqlDatabase.Query(`SELECT email, description FROM nixie.contact_form`)
	if err != nil {
		log.Fatal("Cannot retrieve data from DB", err.Error())
	}

	defer rows.Close()

	items := []*pb.EmailContact{}
	for rows.Next() {
		var email, description string
		if err := rows.Scan(&email, &description); err != nil {
			return nil, err
		}

		items = append(items, &pb.EmailContact{
			Email:       email,
			Description: description,
		})
	}

	return &pb.EmailContactList{
		EmailContacts: items,
	}, nil
}
