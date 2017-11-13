#!make

PWD=$(shell pwd)

all: init build up
#all: dotfiles up
.PHONY: all

init:

build: build-api
 
build-api:
	docker run -it --rm --name my-maven-project \
		-v $(PWD):/usr/src/mymaven \
		-v $(PWD)/m2:/root/.m2 \
		-w /usr/src/mymaven \
		maven:3 bash -c "mvn package"
	make -C collections-api
   
up:
	#docker-compose up -d#
	docker-compose up -d proxy
	docker-compose up -d db 
	docker-compose up -d api 

down:
	docker-compose down
 
 
	
# docker login 
release: 
	docker push dina/collections:v0.1

