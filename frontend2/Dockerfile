# Stage 1: Compile and Build angular codebase

# Use official node image as the base image
FROM node:20.18 as build

# Set the working directory
WORKDIR /usr/local/app

# Add the source code to app
COPY ./ /usr/local/app/

# Install all the dependencies
RUN npm install

# Generate the build of the application
RUN npm run build --prod

# Stage 2: Serve app with nginx server

# Use official nginx image as the base image
FROM nginx:latest

# Remove the default Nginx files.
RUN rm -rf /usr/share/nginx/html/*

# Copy the build output to replace the default nginx contents.
COPY --from=build /usr/local/app/dist/frontend2/browser/ /usr/share/nginx/html

COPY ./nginx.conf /etc/nginx/nginx.conf
# Expose port 80
EXPOSE 80
