package main

import (
	"context"
	"fmt"

	pb "github.com/OSAlt/geekbeacon/service/autogen"

	"log"
	"net"

	// "golang.org/x/net/context"
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

// server is used to implement customer.CustomerServer.
type GrpcServer struct {
	// savedCustomers []*pb.CustomerRequest
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
	pb.RegisterSocialServer(s, &GrpcServer{})
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
