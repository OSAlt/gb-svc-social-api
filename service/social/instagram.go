package social

import (
	"encoding/json"
	"fmt"
	"net/http"

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
	client := &http.Client{}
	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return 0, err
	}

	q := req.URL.Query()
	q.Add("__a", "1")
	req.URL.RawQuery = q.Encode()

	req.Header.Set("content-type", "application/json")

	response, err := client.Do(req)
	if err == nil {
		var data interface{}
		err := json.NewDecoder(response.Body).Decode(&data)
		if err != nil {
			return 0, err
		}
		// data, _ := ioutil.ReadAll(response.Body)
		followers, _ := jsonpath.Read(data, "$.graphql.user.edge_followed_by.count")
		f64 := followers.(float64)
		return int64(f64), nil
	}

	return 0, err

}
