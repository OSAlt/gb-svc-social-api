## TODO:: add documentation.

1. ln -s development.yml docker-compose.yml
2. Database migrations: dbmate --url "postgres://postgres:secret@127.0.0.1:5432/socialdb?sslmode=disable"  up
3. go generate (if db model changed)
3. Update .env file
3a. If running locally export env: 	export $(sed -e '/^#/d' -e '/^$/d' ".env" | xargs)
4. docker-compose build; docker-compose up -d  OR
5. go build && ./gb-svc-social-api

