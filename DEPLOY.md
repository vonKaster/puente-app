# Deployment Instructions

## Prerequisites

- Docker and Docker Compose installed
- Ports 80 and 443 open
- DNS A record pointing to server IP

## Setup

1. Clone the repository:
```bash
git clone -b deploy-vps git@github.com:vonKaster/puente-app.git
cd puente-app
```

2. Configure environment:
```bash
cp .env.production.example .env
nano .env
```

3. Initialize SSL certificate:
```bash
chmod +x init-letsencrypt.sh
./init-letsencrypt.sh
```

4. Start services:
```bash
docker-compose -f docker-compose.prod.yml up --build -d
```

## Verify

```bash
docker ps
curl https://puente-challenge.francaminos.com/actuator/health
```

## Update

```bash
docker-compose -f docker-compose.prod.yml down
git pull origin deploy-vps
docker-compose -f docker-compose.prod.yml up --build -d
```

## Logs

```bash
docker-compose -f docker-compose.prod.yml logs -f
```

