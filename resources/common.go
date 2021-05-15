package resources

import "net/http"

func AllGood(w http.ResponseWriter) http.ResponseWriter {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	return w

}
