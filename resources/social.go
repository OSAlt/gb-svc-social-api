package resources

import (
	"encoding/json"
	"net/http"
	"strconv"
	"strings"

	"github.com/OSAlt/gb-svc-social-api/services"
)

func SocialCountAll(w http.ResponseWriter, r *http.Request) {
	svc := services.GetServices()
	result := svc.SocialCount()

	json.NewEncoder(w).Encode(result)
}

func SocialInstagramActivity(w http.ResponseWriter, r *http.Request) {
	svc := services.GetServices()
	// result := svc.InstagramSocialActivity()
	limits, _ := r.URL.Query()["count"]
	limit := strings.Join(limits, ",")
	i, err := strconv.Atoi(limit)
	if err != nil {
		i = 20
	}

	str := svc.InstagramSocialActivity(i)
	w = AllGood(w)
	json.NewEncoder(w).Encode(str)

}
