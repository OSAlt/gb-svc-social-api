package main

import (
	"context"
	"fmt"
	"net/http"
	"net/smtp"
	"os"
	"path"
	"time"

	"github.com/google/uuid"
	_ "github.com/lib/pq"
	"github.com/shaj13/go-guardian/auth"
	"github.com/shaj13/go-guardian/auth/strategies/basic"
	"github.com/shaj13/go-guardian/auth/strategies/bearer"
	"github.com/shaj13/go-guardian/store"
	log "github.com/sirupsen/logrus"
	"github.com/spf13/viper"

	"github.com/OSAlt/gb-svc-social-api/resources"
	"github.com/OSAlt/gb-svc-social-api/services"
	"github.com/gorilla/handlers"

	"github.com/gorilla/mux"
)

type App struct {
	Router        *mux.Router
	S             *services.Services
	Authenticator auth.Authenticator
	Cache         store.Cache
}

func (a *App) Initialize(user, password, dbname string) {
	a.S = &services.Services{}
	a.S.DB = services.Initialize(user, password, dbname)
	a.Router = mux.NewRouter().StrictSlash(true)
	a.setupGoGuardian()
	a.setupEmail()
	a.initializeRoutes()
}

func (a *App) setupEmail() {
	emailEnabled := viper.GetBool("email.enabled")
	if emailEnabled == false {
		return
	}
	host := viper.GetString("email.host")
	username := viper.GetString("email.username")
	password := viper.GetString("email.password")
	auth := smtp.PlainAuth("", username, password, host)
	a.S.Email = auth
}

func validateUser(ctx context.Context, r *http.Request, userName, password string) (auth.Info, error) {
	if userName == "medium" && password == "medium" {
		return auth.NewDefaultUser("medium", "1", nil, nil), nil
	}
	return nil, fmt.Errorf("Invalid credentials")
}

func (a *App) setupGoGuardian() {
	a.Authenticator = auth.New()
	a.Cache = store.NewFIFO(context.Background(), time.Minute*10)
	basicStrategy := basic.New(validateUser, a.Cache)
	tokenStrategy := bearer.New(bearer.NoOpAuthenticate, a.Cache)
	a.Authenticator.EnableStrategy(basic.StrategyKey, basicStrategy)
	a.Authenticator.EnableStrategy(bearer.CachedStrategyKey, tokenStrategy)
}

func (a *App) Run(addr string) {
	a.initializeRoutes()
	corsObj := handlers.AllowedOrigins([]string{"*"})

	log.Infof("starting server on: http://%s", addr)
	log.Fatal(http.ListenAndServe(addr, handlers.CORS(corsObj)(a.Router)))
}

func (a *App) createToken(w http.ResponseWriter, r *http.Request) {
	token := uuid.New().String()
	user := auth.NewDefaultUser("medium", "1", nil, nil)
	tokenStrategy := a.Authenticator.Strategy(bearer.CachedStrategyKey)
	auth.Append(tokenStrategy, token, user, r)
	body := fmt.Sprintf("token: %s \n", token)
	w.Write([]byte(body))
}

func (a *App) middleware(next http.Handler) http.HandlerFunc {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		log.Println("Executing Auth Middleware")
		user, err := a.Authenticator.Authenticate(r)
		if err != nil {
			code := http.StatusUnauthorized
			http.Error(w, http.StatusText(code), code)
			return
		}
		log.Printf("User %s Authenticated\n", user.UserName())
		next.ServeHTTP(w, r)
	})
}

func testing(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	id := vars["id"]
	books := map[string]string{
		"1449311601": "Ryan Boyd",
		"148425094X": "Yvonne Wilson",
		"1484220498": "Prabath Siriwarden",
	}
	body := fmt.Sprintf("Author: %s \n", books[id])
	w.Write([]byte(body))
}

func (a *App) initializeRoutes() {
	a.Router.HandleFunc("/auth/token", a.createToken).Methods("GET")
	a.Router.HandleFunc("/testing/{id}", a.middleware(http.HandlerFunc(testing))).Methods("GET")

	apiV1 := a.Router.PathPrefix("/api/v1.0").Subrouter()
	apiV1.HandleFunc("/contact/list", resources.ContactList).Methods(http.MethodGet)

	apiV1.HandleFunc("/social/count/all", resources.SocialCountAll).Methods(http.MethodGet)
	apiV1.HandleFunc("/social/INSTAGRAM/activity", resources.SocialInstagramActivity).Methods(http.MethodGet)

	cwd, _ := os.Getwd()
	dest := path.Join(cwd, "static")
	log.Info(dest)

	fileHandler := http.FileServer(http.Dir(dest))
	a.Router.PathPrefix("/static/").Handler(fileHandler)
	a.Router.PathPrefix(dest).Handler(http.StripPrefix(dest, http.FileServer(http.Dir("./static"))))
	a.Router.HandleFunc("/", resources.Redirect)

}
