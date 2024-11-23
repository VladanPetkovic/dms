FROM node:19-alpine as frontend_build
WORKDIR /app
COPY frontend/web_ui/package*.json ./
RUN npm install
COPY frontend/web_ui/ ./
RUN npm run build
FROM nginx:latest
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
COPY --from=frontend_build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]