swagger:
	swagger generate spec -o ./static/swagger.json

build: clean 
	go generate
	go build -o social_svc

clean:
	rm -f social_svc social_svc_linux


linux: clean 
	env GOOS='linux' GOARCH='amd64' go build -o social_svc_linux