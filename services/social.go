package services

import (
	"encoding/json"
	"fmt"
	"os"

	"github.com/OSAlt/gb-svc-social-api/models"
	log "github.com/sirupsen/logrus"
	"github.com/yalp/jsonpath"
)

const INSTAGRAM_URL_FORMAT = "https://www.instagram.com/p/%s/"

func (s *Services) SocialCount() models.SocialCount {
	count := models.SocialCount{}
	count.Youtube = 330000
	count.Twitter = 31102
	count.Twitch = 2500
	count.Discord = 6336
	count.Facebook = 50466
	count.Instagram = 7498

	return count
}

func populateDimensions(entry *models.SocialActivity, values map[string]interface{}) {
	if mapVal, ok := values["dimensions"]; ok {
		entry.Dimensions = models.MediaDimensions{}
		height := mapVal.(map[string]interface{})["height"]
		width := mapVal.(map[string]interface{})["width"]
		switch v := height.(type) {
		case float64:
			entry.Dimensions.Height = v
		}

		switch v := width.(type) {
		case float64:
			entry.Dimensions.Height = v
		}

	}
}

func (s *Services) InstagramSocialActivity(limit int) []models.SocialActivity {
	data, err := os.ReadFile("static/instagram.json")
	if err != nil {
		log.Fatal(err)
	}

	var bookstore interface{}
	err = json.Unmarshal(data, &bookstore)
	array, err := jsonpath.Read(bookstore, "$.graphql.user.edge_owner_to_timeline_media.edges[*].node")
	result := make([]models.SocialActivity, 0)

	//return data
	count := 0
	for _, val := range array.([]interface{}) {
		entry := models.SocialActivity{}
		values := val.(map[string]interface{})
		entry.MediaUrl = values["display_url"].(string)
		entry.SocialLove = make(map[string]interface{})
		countInt, _ := jsonpath.Read(val, "$.edge_media_to_comment.count")
		entry.SocialLove["comments"] = countInt
		countInt, _ = jsonpath.Read(val, "$.edge_liked_by.count")
		entry.SocialLove["likes"] = countInt
		countInt, _ = jsonpath.Read(val, "$.edge_media_to_caption.edges[0].node.text")
		entry.Text = countInt.(string)
		shortCode := values["shortcode"]
		url := fmt.Sprintf(INSTAGRAM_URL_FORMAT, shortCode.(string))
		entry.SourceUri = url
		entry.IsVideo = values["is_video"].(bool)

		result = append(result, entry)
		count++
		if count >= limit {
			break
		}

	}

	return result

}
