package social

import (
	"fmt"

	"github.com/yalp/jsonpath"
)

const (
	DISCORD_URL    = "https://discordapp.com/api/v6/invites/%s"
	DISCORD_SERVER = "geekbeacon"
)

type Discord struct{}

func (s *Discord) FollowerCount() (int64, error) {
	url := fmt.Sprintf(DISCORD_URL, DISCORD_SERVER)
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