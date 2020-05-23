package models

import dbModels "github.com/OSAlt/gb-svc-social-api/dbmodels/nixie/models"

type Contact struct {
	Domain      string `json:"-"`
	Email       string `json:"email"`
	Description string `json:"description"`
}

func Generate(source *dbModels.ContactForm) Contact {
	c := Contact{}
	c.Domain = source.Domain
	c.Description = source.Description
	c.Email = source.Email

	return c
}
