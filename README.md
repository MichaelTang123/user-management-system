# RESTApi Demo Project
User Management RESTApi application demo


# Build and run demo
## Build application docker image
cd into top directory of the project where the docker-compose.yml is located in and execute:

```
mvn clean install
```

# Start DB and application
```
docker-compose up
```

# Docs
The api spec doc(OpenAPI) will be generated/updated into directory of ${project-root}/docs after successfully build.
Another way to view api spec online once you started services by commaond: docker-compose up.

## View user API doc:
http://docker-app-host:8000/api/swagger-ui/index.html#/

## View user API specifications online:
http://docker-app-host:8000/v3/api-docs

## View event API doc:
http://docker-app-host:8001/api/swagger-ui/index.html#/

## View event API specifications online:
http://docker-app-host:8001/v3/api-docs

# Run Test
you can run test by executing:
```
mvn test
```

## Unit Test Cases:
Source code: ${project-root}/user-service/src/test/java/com/michaeltang/restapi/test/ut
Test report: ${project-root}/user-service/target/surefire-reports

## BDD Test Cases:
Cucumber Feature Spec: ${project-root}/user-service/src/test/resources/features
Test report: ${project-root}/user-service/target/cucumber-reports.html



# Try the API 
Please replace the docker-app-host with the actual ip address of your docker. See the example below:

```
192.168.9.10   docker-app-host
```


## Register new user
```
curl -v -X POST docker-app-host:8000/api/users -d "{\"id\": \"user1\",\"firstName\": \"FirstName\",\"lastName\": \"LastName\",\"email\": \"user1@demo.email.com\"}" -H "Content-Type:application/json"
```

## Fetch pageable users:
```
curl -v docker-app-host:8000/api/users?page=0
```

## Query user by id:
```
curl -v docker-app-host:8000/api/users/user1
```

## Edit user by id:
```
curl -v -X PUT docker-app-host:8000/api/users/user1 -d "{\"email\": \"user1@demo.email.com\"}" -H "Content-Type:application/json"
```

## Delete user by id:
```
curl -v -X DELETE docker-app-host:8000/api/users?users=user1,user2
```

## Fetch pageable events:
```
curl -v docker-app-host:8001/api/events?page=0
```



