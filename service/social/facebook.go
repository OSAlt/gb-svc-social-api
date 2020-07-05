package social

import (
	pb "github.com/OSAlt/geekbeacon/service/autogen"
	"github.com/OSAlt/geekbeacon/service/common"
)

type Facebook struct{}

func (s *Facebook) FollowerCount() (int64, error) {
	return int64(50466), nil
}

func (s *Facebook) GetSocialType() string {
	return "facebook"
}

func (s *Facebook) GetSocialActivity(limit *common.Pagination) (*pb.SocialActivityList, error) {
	result := pb.SocialActivityList{}
	return &result, nil

}