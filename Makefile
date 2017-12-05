#!make
PWD=$(shell pwd)

#all:  build up-dev
all: build up

.PHONY: all

init:
	@echo "working directory is ${PWD}"

build: build-api

build-api:
	docker run -it --rm --name my-maven-project \
		-v $(PWD):/usr/src/mymaven \
		-v $(PWD)/m2:/root/.m2 \
		-w /usr/src/mymaven \
		maven:3 bash -c "mvn package"
	make -C collections-api

up:
	docker-compose -f docker-compose.yml up -d db
	docker-compose -f docker-compose.yml up -d api

up-prod:
	docker-compose -f docker-compose.yml up -d db
	docker-compose -f docker-compose.yml up -d api

up-dev:
	docker-compose -f docker-compose.dev.yml up -d proxy-local
	docker-compose -f docker-compose.dev.yml up -d db
	docker-compose -f docker-compose.dev.yml up -d api

down:
	docker-compose -f docker-compose.yml down

down-prod:
	docker-compose -f docker-compose.yml down

down-dev:
	docker-compose -f docker-compose.dev.yml down

test-access_get-request:
	cd scripts && ./get_response.sh

test-access_post-request:
	cd scripts && ./get_response.sh

# docker login
release:
	docker push dina/collections:v0.1
