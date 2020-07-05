package social

import (
	"errors"
)

//NOTE: this adapter pattern is inherited from Java.  It's likely an anti-pattern in go,
// but for the initial phase i'm porting it as is.

type SocialType string

const (
	FacebookType  SocialType = "facebook"
	TwitterType   SocialType = "twitter"
	InstagramType SocialType = "instagram"
	YoutubeType   SocialType = "youtube"
	DiscordType   SocialType = "discord"
	TwitchType    SocialType = "twitch"
	AggregateType SocialType = "aggregate"
)

var SocialLookup map[SocialType]SocialDataService

func init() {
	SocialLookup = make(map[SocialType]SocialDataService)
	SocialLookup[InstagramType] = (*Instagram)(nil)
	SocialLookup[FacebookType] = (*Facebook)(nil)
	SocialLookup[DiscordType] = (*Discord)(nil)
	SocialLookup[YoutubeType] = nil
}

func (s SocialType) IsValid() bool {
	_, err := GetSocialType(string(s))
	if err != nil {
		return false
	}
	return true
}

func GetSocialType(typeRequest string) (SocialType, error) {

	for k, _ := range SocialLookup {
		str := string(k)
		if str == typeRequest {
			return k, nil

		}
	}

	return "", errors.New("socialType was not found")

}

func GetSocialService(socialType SocialType) (SocialDataService, error) {
	obj, ok := SocialLookup[socialType]
	if !ok {
		return nil, errors.New("invalid, social service requested")
	}
	return obj, nil
}
