version: '3.8'

services:
  mysql-db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: fixypro
      MYSQL_USER: fixypro
      MYSQL_PASSWORD: fixypro
    volumes:
      - ./mysql-data:/var/lib/mysql
    ports:
      - "3310:3306"

  app:
    build:
      context: .
      dockerfile: Dockerfile
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql-db:3306/fixypro
      SPRING_DATASOURCE_USERNAME: fixypro
      SPRING_DATASOURCE_PASSWORD: fixypro
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
    ports:
      - "8085:8080"
    depends_on:
      - mysql-db