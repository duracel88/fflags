# Feature flags 
## Prerequisites
- java 17
- gradle
## Run
```
$ gradle bootRun
```
or
```
$ gradle build
$ java -jar build/libs/fflags-0.0.1-SNAPSHOT.jar
```
## API

### Create feature flag
```
[Path]      /fflags
[Method]    POST 
[Auth]      Basic
[Role]      ADMIN
[Suc res]   201
[Body]
{
    "name": "string", (not empty)
    "description": "string" (not empty)
} 
[Response]
{
    "globallyEnabled": "boolean",
    "name": "string", 
    "description": "string"
} 
```

### Get feature flags, mix of assigned to me and globally enabled
```
[Path]      /fflags
[Method]    GET 
[Auth]      Basic
[Role]      USER, ADMIN
[Suc res]   200
[Fail res]  400 - bad request body
[Param]     my=true
[Param]     global=true
[Response]
[{
    "globallyEnabled": "boolean",
    "name": "string", 
    "description": "string"
]}
```

### Assign flag to user
```
[Path]      /fflags/{flagName}/assignee
[Path var]  flagName
[Method]    POST 
[Auth]      Basic
[Role]      ADMIN
[Succ res]  201
[Fail res]  404 - flagName not found
[Fail res]  400 - bad request body
[Body]
{
    "assigneeUsername": "string", (not empty)
} 
```

### Deallocate flag from user
```
[Path]      /fflags/{flagName}/assignee/{username}
[Path var]  username
[Path var]  flagName
[Method]    DELETE 
[Auth]      Basic
[Role]      ADMIN
[Suc res]   204
[Fail res]  404 - flagName not found
[Fail res]  404 - assignee not found

```