https://github.com/quarkusio/quarkus/issues/6168

> ./mvnw compile quarkus:dev

check what you get: curl http://localhost:8080/my-project-service/performance/resoruce

Then:

> ./mvnw package -Dnative -Dquarkus.profile=dev -Dmaven.test.skip=true

Coffee...

Then run:

> cd my-project-backend

> ./target/my-project-backend-1.0.0-runner


check what you get: 

> curl http://localhost:8080/my-project-service/performance/resoruce