package social

import (
	"reflect"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestGetSocialType(t *testing.T) {
	request := "instagram"
	res, err := GetSocialType(request)
	assert.Nil(t, err)
	assert.Equal(t, res, InstagramType)
	resType := reflect.TypeOf(res)
	assert.Equal(t, resType.String(), "social.SocialType")
	request = "invalid"
	res, err = GetSocialType(request)
	assert.Equal(t, res, SocialType(""))
	assert.NotNil(t, err)
}

func TestIsValid(t *testing.T) {
	v := SocialType("invalid")
	valid := v.IsValid()
	assert.False(t, valid)
	valid = InstagramType.IsValid()
	assert.True(t, valid)

}

func TestGetSocialService(t *testing.T) {
	v := SocialType("invalid")
	res, err := GetSocialService(v)
	assert.NotNil(t, err)
	res, _ = GetSocialService(FacebookType)
	cnt, _ := res.FollowerCount()
	assert.Equal(t, cnt, int64(50466))

}
