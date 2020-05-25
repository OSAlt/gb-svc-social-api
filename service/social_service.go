package main

import (
	"context"
	"errors"

	pb "github.com/OSAlt/geekbeacon/service/autogen"
	"github.com/OSAlt/geekbeacon/service/social"
	"google.golang.org/protobuf/types/known/emptypb"
)

func (s *GrpcServer) GetSocialTypes(ctx context.Context, empty *emptypb.Empty) (*pb.SocialTypeList, error) {
	listItems := []*pb.SocialType{}

	for _, item := range social.SocialTypes {
		listItems = append(listItems, &pb.SocialType{
			Type: string(item),
		})
	}

	return &pb.SocialTypeList{
		Types: listItems,
	}, nil
}

func (s *GrpcServer) GetSocialCount(ctx context.Context, request *pb.SocialType) (*pb.SocialCountResponse, error) {

	var socialType social.SocialType
	if !social.SocialType(request.Type).IsValid() {
		return nil, errors.New("invalid, unsupported social service requested")
	} else {
		socialType, _ = social.GetSocialType(request.Type)
	}

	obj, err := social.GetSocialService(socialType)
	if err != nil {
		return nil, errors.New("invalid, Could not obtain a valid handler")
	}

	res, err := obj.FollowerCount()

	if err != nil {
		return nil, err
	}

	response := pb.SocialCountResponse{
		Count: res,
	}

	return &response, nil

}
