package main

import (
	"context"

	pb "github.com/OSAlt/geekbeacon/service/autogen"
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
