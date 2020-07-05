package social

import (
	"fmt"
	pb "github.com/OSAlt/geekbeacon/service/autogen"
	"github.com/OSAlt/geekbeacon/service/common"

	"github.com/spf13/viper"
	"github.com/yalp/jsonpath"
)

type Discord struct{}

func (s *Discord) FollowerCount() (int64, error) {
	user := viper.GetString("social.discord.username")
	baseUrl := viper.GetString("social.discord.url")
	url := fmt.Sprintf(baseUrl, user)
	parameters := map[string]string{
		"with_counts": "true",
	}

	data, err := loadRemoteData(url, parameters, "GET")

	if err != nil {
		return 0, err
	}

	followers, _ := jsonpath.Read(data, "$.approximate_member_count")
	f64 := followers.(float64)
	return int64(f64), nil

}

func (s *Discord) GetSocialType() string {
	return "discord"
}

func (s *Discord) GetSocialActivity(limit *common.Pagination) (*pb.SocialActivityList, error) {
	result := pb.SocialActivityList{}
	return &result, nil

}