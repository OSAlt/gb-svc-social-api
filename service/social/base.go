package social

import (
	"encoding/json"
	"log"
	"net/http"
)

type SocialDataService interface {
	FollowerCount() (int64, error) //return follower count
	GetSocialType() string
	// authorizeApplication()  Authorize the application to access user data or social media api at least on behalf of user
	// void saveRequestToken(String token, String verifier)  persist data to DB
	//  List<SocialActivity> getSocialActivity(limit int);
}

func loadRemoteData(url string, parameters map[string]string, requestType string) (interface{}, error) {
	var data interface{}
	client := &http.Client{}
	req, _ := http.NewRequest(requestType, url, nil)
	req.Header.Set("content-type", "application/json")
	if requestType == "GET" {
		q := req.URL.Query()
		for k, v := range parameters {
			q.Add(k, v)
		}

		req.URL.RawQuery = q.Encode()
	}
	response, err := client.Do(req)
	err = json.NewDecoder(response.Body).Decode(&data)
	if err != nil {
		log.Fatalf("failed to decode JSON body %s", err)
		return nil, err
	}

	return data, nil
}
