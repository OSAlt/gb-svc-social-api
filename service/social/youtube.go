package social

import (
	"fmt"
	"log"

	"github.com/spf13/viper"
)

type Youtube struct{}

func (s *Youtube) FollowerCount() (int64, error) {
	user := viper.GetString("social.youtube.username")
	baseUrl := viper.GetString("social.youtube.url")
	url := fmt.Sprintf(baseUrl, user)
	log.Println(url)
	// parameters := map[string]string{
	// 	"part":        "id,statistics",
	// 	"forUsername": "channel",
	// 	"key":         "api",
	// }

	// data, err := loadRemoteData(url, parameters, "GET")

	// if err != nil {
	// 	return 0, err
	// }

	// followers, _ := jsonpath.Read(data, "$.graphql.user.edge_followed_by.count")
	// f64 := followers.(float64)
	// return int64(f64), nil
	return 0, nil

}

func (s *Youtube) GetSocialType() string {
	return "youtube"
}
