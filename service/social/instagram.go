package social

import (
	"fmt"

	"github.com/yalp/jsonpath"
)

type Instagram struct{}

const (
	INSTAGRAM_URL_FORMAT = "https://www.instagram.com/p/%s/"
	INSTAGRAM_API_URL    = "https://www.instagram.com/%s/"
	USERNAME             = "nixietron"
)

func (s *Instagram) FollowerCount() (int64, error) {
	url := fmt.Sprintf(INSTAGRAM_API_URL, USERNAME)
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
