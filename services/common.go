package services

import (
	"database/sql"
	"fmt"
	"net/smtp"

	log "github.com/sirupsen/logrus"
)

type Services struct {
	DB    *sql.DB
	Email smtp.Auth
}

var instance *Services

func GetServices() *Services {
	if instance != nil {
		return instance
	} else {
		log.Panic("Error, instance is nil")
	}

	return nil
}

func Initialize(user, password, dbname string) *sql.DB {
	connectionString :=
		fmt.Sprintf("user=%s password=%s dbname=%s sslmode=disable", user, password, dbname)

	var err error
	instance = &Services{}
	instance.DB, err = sql.Open("postgres", connectionString)
	if err != nil {
		log.Fatal(err)
	}

	return instance.DB

}
