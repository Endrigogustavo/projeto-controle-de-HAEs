FROM node:22.15.0

ARG PORT
ENV PORT=${PORT}

WORKDIR /app

COPY package*.json ./

COPY . .

RUN npm install

RUN npm run generate

CMD ["npm", "start"]