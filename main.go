package main

//go:generate rm -fr dbmodels
//go:generate mkdir -p dbmodels/nixie/models
//go:generate mkdir -p dbmodels/config/models
//go:generate sqlboiler --wipe psql -c conf/dbconf/config_boiler.toml
//go:generate sqlboiler --wipe psql -c conf/dbconf/nixie_boiler.toml
import (
	"fmt"
	"os"

	log "github.com/sirupsen/logrus"
	"github.com/spf13/viper"
)

func main() {
	a := App{}
	a.Initialize(
		os.Getenv("POSTGRES_USER"),
		os.Getenv("POSTGRES_PASSWORD"),
		os.Getenv("POSTGRES_DB"),
		os.Getenv("DB_HOST"))

	port := os.Getenv("SERVICE_PORT")
	if port == "" {
		port = "8012"
	}
	a.Run(fmt.Sprintf("0.0.0.0:%s", port))
}

func init() {
	viper.SetConfigName("app") // name of config file (without extension)
	viper.AddConfigPath("conf")
	viper.AddConfigPath(".")

	err := viper.ReadInConfig() // Find and read the config file
	if err != nil {
		log.Fatal(err)
	}

}
