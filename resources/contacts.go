package resources

import (
	"encoding/json"
	"net/http"
	"strings"

	"github.com/OSAlt/gb-svc-social-api/services"
)

func ContactList(w http.ResponseWriter, r *http.Request) {
	svc := services.GetServices()
	domains, _ := r.URL.Query()["domain"]
	domain := strings.Join(domains, ",")

	result := svc.ContactList(domain)

	w = AllGood(w)
	if result != nil {
		json.NewEncoder(w).Encode(result)
	}

}
