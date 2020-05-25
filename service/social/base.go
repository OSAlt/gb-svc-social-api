package social

import (
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"
)

type SocialDataService interface {
	FollowerCount() (int64, error) //return follower count
	// authorizeApplication()  Authorize the application to access user data or social media api at least on behalf of user
	// void saveRequestToken(String token, String verifier)  persist data to DB
	//  List<SocialActivity> getSocialActivity(limit int);
}

func loadRemoteData(url string) ([]byte, error) {
	client := &http.Client{}
	req, _ := http.NewRequest("GET", url, nil)
	req.Header.Set("content-type", "application/json")
	response, err := client.Do(req)
	if err == nil {
		data, _ := ioutil.ReadAll(response.Body)
		return data, nil
	}

	msg := fmt.Sprintf("The HTTP request failed with error %s", err.Error())
	return nil, errors.New(msg)
}
