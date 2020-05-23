package resources

import (
	"net/http"
)

func Redirect(w http.ResponseWriter, r *http.Request) {
	http.Redirect(w, r, "https://www.geekbeacon.org", http.StatusPermanentRedirect)
}
