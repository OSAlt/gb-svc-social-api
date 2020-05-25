package social

import (
	"fmt"

	"github.com/spf13/viper"
	"github.com/yalp/jsonpath"
)

type Instagram struct{}

func (s *Instagram) FollowerCount() (int64, error) {
	user := viper.GetString("social.instagram.username")
	baseUrl := viper.GetString("social.instagram.url")
	url := fmt.Sprintf(baseUrl, user)
	parameters := map[string]string{
		"__a": "1",
	}

	data, err := loadRemoteData(url, parameters, "GET")

	if err != nil {
		return 0, err
	}

	followers, _ := jsonpath.Read(data, "$.graphql.user.edge_followed_by.count")
	f64 := followers.(float64)
	return int64(f64), nil

}

func (s *Instagram) GetSocialType() string {
	return "instagram"
}
