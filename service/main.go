package main

import (
	"os"

	pb "github.com/OSAlt/geekbeacon/service/autogen"
	"github.com/spf13/viper"

	_ "github.com/lib/pq"

	"context"
	"database/sql"
	"fmt"
	"log"
	"net"

	"google.golang.org/grpc"

	"flag"
	"net/http"
	"sync"

	"github.com/golang/glog"
	"github.com/grpc-ecosystem/grpc-gateway/runtime"
)

const (
	grpcPort = ":50051"
)

var wg sync.WaitGroup

var (
	grpcServerEndpoint = flag.String("grpc-server-endpoint", "localhost:50051", "gRPC server endpoint")
)

//GrpcServer server is used to implement GRPC service
type GrpcServer struct {
	sqlDatabase *sql.DB
}

//NewService Create a new instance of DB with the DB connection
func NewService() *GrpcServer {
	srv := GrpcServer{}

	dbPassword := os.Getenv("POSTGRES_PASSWORD")
	dbHost := os.Getenv("DB_HOST")

	dataSourceName := fmt.Sprintf("host=%s user=postgres dbname=socialdb password=%s sslmode=disable", dbHost, dbPassword)
	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		log.Fatal(err)
	}
	srv.sqlDatabase = db

	return &srv
}

func startGrpc() {
	defer wg.Done()

	fmt.Println("Starting grpc server on port: ", grpcPort)
	lis, err := net.Listen("tcp", grpcPort)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	// Creates a new gRPC server

	s := grpc.NewServer()
	server := NewService()
	pb.RegisterSocialServer(s, server)
	s.Serve(lis)

}

//https://github.com/grpc-ecosystem/grpc-gateway
func runWww() error {
	ctx := context.Background()
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()
	// fs := http.FileServer(http.Dir("./static"))

	// Register gRPC server endpoint
	// Note: Make sure the gRPC server is running properly and accessible
	// mux := runtime.NewServeMux()
	fmt.Println("Starting web gateway server on port: 8081")
	mux := runtime.NewServeMux(
		// runtime.WithMarshalerOption("application/json+pretty", &runtime.JSONPb{Indent: "  "}),
		runtime.WithMarshalerOption(runtime.MIMEWildcard, &runtime.JSONPb{OrigName: false}),
	)

	opts := []grpc.DialOption{grpc.WithInsecure()}
	err := pb.RegisterSocialHandlerFromEndpoint(ctx, mux, *grpcServerEndpoint, opts)
	if err != nil {
		return err
	}

	// Start HTTP server (and proxy calls to gRPC server endpoint)
	return http.ListenAndServe(":8081", mux)
	// return http.ListenAndServe(":8081", prettier(mux))

}

func startWww() {
	defer wg.Done()
	flag.Parse()
	defer glog.Flush()

	if err := runWww(); err != nil {
		glog.Fatal(err)
	}

}

func main() {
	wg.Add(2)
	go startGrpc()
	go startWww()

	wg.Wait()
}

func init() {
	viper.SetConfigName("app") // name of config file (without extension)
	// viper.SetConfigType("yaml") // or viper.SetConfigType("YAML")
	viper.AddConfigPath("app.yaml")
	viper.AddConfigPath(".")

	err := viper.ReadInConfig() // Find and read the config file
	if err != nil {
		log.Fatal(err)
	}

}
