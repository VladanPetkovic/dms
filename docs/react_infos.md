# Frontend information

[<kbd>&larr; Go Back</kbd>](../README.md)

### Create a frontend-build

When you look in the docker-compose-file, you will see, that nginx is mapped to the build folder.
Therefor we need to create the frontend build using:

First go the src/main/frontend/web_ui folder, then run:
```
npm run build
```