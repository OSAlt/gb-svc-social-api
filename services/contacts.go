package services

import (
	"context"

	nixieModel "github.com/OSAlt/gb-svc-social-api/dbmodels/nixie/models"
	"github.com/OSAlt/gb-svc-social-api/models"
	"github.com/sirupsen/logrus"
	"github.com/volatiletech/sqlboiler/v4/queries/qm"
)

func (s *Services) ContactList(domain string) []models.Contact {
	ctx := context.Background()
	if domain == "" {
		domain = "nixiepixel.com"
	}
	res, err := nixieModel.ContactForms(qm.Where("domain=?", domain)).All(ctx, s.DB)
	if err != nil {
		logrus.Panic("Cannot get contact forms data")
	}
	result := make([]models.Contact, 0)
	for _, val := range res {
		c := models.Generate(val)
		result = append(result, c)
	}
	return result
}
