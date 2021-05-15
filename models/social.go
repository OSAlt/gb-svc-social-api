package models

type SocialCount struct {
	Youtube   uint32 `json:"youtube"`
	Twitter   uint32 `json:"twitter"`
	Facebook  uint32 `json:"facebook"`
	Twitch    uint32 `json:"twitch"`
	Discord   uint32 `json:"discord"`
	Instagram uint32 `json:"instagram"`
}

type SocialActivity struct {
	SourceUri string `json:"sourceUri"`
	MediaUrl  string `json:"mediaUrl"`

	Text string `json:"text"`

	Dimensions MediaDimensions        `json:"dimensions"`
	IsVideo    bool                   `json:"video"`
	SocialLove map[string]interface{} `json:"socialLove"`
}

type MediaDimensions struct {
	Height float64 `json:"height"`
	Width  float64 `json:"width"`
}
