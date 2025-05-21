FROM node:22.15.0

ARG PORT
ENV PORT=${PORT}

WORKDIR /app

COPY . .

RUN npm install
RUN npm run generate
RUN npm run migrate

EXPOSE ${PORT}

CMD ["npm", "start"]
