package social

import (
	"fmt"
	pb "github.com/OSAlt/geekbeacon/service/autogen"
	"github.com/OSAlt/geekbeacon/service/common"
	"github.com/spf13/viper"
	"github.com/yalp/jsonpath"
	"strconv"
	"unicode/utf8"
)


type Instagram struct{}

func getInstagramRequest() (string, map[string]string) {
	user := viper.GetString("social.instagram.username")
	baseUrl := viper.GetString("social.instagram.url")
	url := fmt.Sprintf(baseUrl, user)
	parameters := map[string]string{
		"__a": "1",
	}

	return url, parameters

}

func (s *Instagram) FollowerCount() (int64, error) {
	url, parameters := getInstagramRequest()

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

func safeExtract(node interface{}, path string, defaultValue interface{}) interface{} {
	result, err := jsonpath.Read(node, path)
	if err != nil {
		return defaultValue
	}

	return  result
}

func sanitizeString(caption interface{}, limit int) string {
	encoded := []rune{}
	aBytes := []byte(caption.(string))
	for len(aBytes) > 0 {
		r, size := utf8.DecodeRune(aBytes)
		encoded= append(encoded, r)

		aBytes = aBytes[size:]
		if len(encoded) >= limit {
			break
		}
	}
	return string(encoded)
}

func (s *Instagram) GetSocialActivity(limit *common.Pagination) (*pb.SocialActivityList, error) {
	result := pb.SocialActivityList{
		Activity: []*pb.SocialActivity{},
	}
	url, parameters := getInstagramRequest()
	data, err := loadRemoteData(url, parameters, "GET")
	postUrl := viper.GetString("social.instagram.post_url")

	if err != nil {
		return nil, err
	}

	activity, _ := jsonpath.Read(data, "$.graphql.user.edge_owner_to_timeline_media.edges[*].node")
	for _, val := range activity.([]interface{}) {
		activity := pb.SocialActivity{}
		var socialLove = make(map[string]string)

		caption, _ := jsonpath.Read(val, "$.edge_media_to_caption.edges[0].node.text")
		activity.Text = sanitizeString(caption, 100)
		valMap := val.(map[string]interface{})
		if shortcode, ok := valMap["shortcode"]; ok && shortcode.(string) != "" {
			post := fmt.Sprintf(postUrl, shortcode.(string))
			activity.SourceUri = post
		}
		if mapVal, ok := valMap["display_url"]; ok && mapVal.(string) != "" {
			activity.MediaUrl = mapVal.(string)
		}
		if mapVal, ok := valMap["is_video"]; ok && mapVal.(bool)  {
			activity.IsVideo= true
		} else {
			activity.IsVideo = false
		}

		if mapVal, ok := valMap["dimensions"]; ok  {
			dimMap := mapVal.(map[string]interface{})
			dims := pb.MediaDimensions{ }
			if height, heightOk := dimMap["height"]; heightOk {
				dims.Height = int32(height.(float64))
			}
			if width, widthOk := dimMap["width"]; widthOk {
				dims.Width = int32(width.(float64))
			}

			activity.Dimensions = &dims
		}

		count := safeExtract(val, "$.edge_media_to_comment.count", 0).(float64)
		socialLove["comments"] = strconv.FormatInt(int64(count), 10)
		count = safeExtract(val, "$.edge_liked_by.count", 0).(float64)
		socialLove["likes"] = strconv.FormatInt(int64(count), 10)

		activity.SocialLove = socialLove
		result.Activity = append(result.Activity, &activity)
	}


	return &result, nil
}

