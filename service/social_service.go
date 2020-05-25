package main

import (
	"context"
	"errors"

	pb "github.com/OSAlt/geekbeacon/service/autogen"
	"github.com/OSAlt/geekbeacon/service/social"
	"google.golang.org/protobuf/types/known/emptypb"
)

type SocialType string

const (
	facebook  SocialType = "facebook"
	twitter   SocialType = "twitter"
	instagram SocialType = "instagram"
	youtube   SocialType = "youtube"
	discord   SocialType = "discord"
	twitch    SocialType = "twitch"
	aggregate SocialType = "aggregate"
)

var SocialTypes = [...]SocialType{instagram}

func (s *GrpcServer) GetSocialTypes(ctx context.Context, empty *emptypb.Empty) (*pb.SocialTypeList, error) {
	listItems := []*pb.SocialType{}

	for _, item := range SocialTypes {
		listItems = append(listItems, &pb.SocialType{
			Type: string(item),
		})
	}

	return &pb.SocialTypeList{
		Types: listItems,
	}, nil
}

func (s *GrpcServer) GetSocialCount(ctx context.Context, socialType *pb.SocialType) (*pb.SocialCountResponse, error) {

	if socialType.Type != "instagram" {
		return nil, errors.New("invalid, unsupported social service requested")
	}
	var obj social.SocialDataService
	obj = (*social.Instagram)(nil)
	res, err := obj.FollowerCount()

	if err != nil {
		return nil, err
	}

	response := pb.SocialCountResponse{
		Count: res,
	}

	return &response, nil

}
